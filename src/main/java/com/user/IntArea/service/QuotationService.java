package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.controller.ImageController;
import com.user.IntArea.dto.image.TestMultiImageDto;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.dto.quotation.QuotationUpdateDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.Progress;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;


@Service
@RequiredArgsConstructor
public class QuotationService {


    private final ImageController imageController;

    private final QuotationRequestService quotationRequestService;

    private final QuotationRepository quotationRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final QuotationRequestRepository quotationRequestRepository;
    private final ImageService imageService;


    // 견적서에 접근하는 멤버가 그 견적서와 연관된 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void isCompanyManager(Quotation quotation) {
        Portfolio portfolio = quotation.getQuotationRequest().getPortfolio();
        if(portfolio == null) {
            throw new NoSuchElementException("포트폴리오가 존재하지 않습니다.");
        }
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        if (!company.equals(portfolio.getCompany())) {
            throw new NoSuchElementException("");
        }
    }

    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        return company;
    }

    // 현재 로그인한 멤버가 해당 견적요청서의 작성자인지 확인
    private boolean isLoggedMemberQuotationRequestWriter(QuotationRequest quotationRequest) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        return member.equals(quotationRequest.getMember());
    }


    // 견적 요청서 작성자 권한

    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 견적서 출력
    public Quotation getValidQuotationForQuotationRequest(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Optional<Quotation> quotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId());
        if(quotation.isEmpty()) {
            throw new NoSuchElementException("유효한 견적서가 없습니다.");
        }
        return quotation.orElse(null);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 견적서를 받은 이후 거래 취소
    public Quotation rejectQuotation(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("유효한 견적서가 없습니다."));

        quotation.setAvailable(false);
        quotation.setQuotationProgress(Progress.USER_CANCELLED);
        return quotationRepository.save(quotation);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 받은 견적서 승인 조치
    public Quotation approveQuotation(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("유효한 견적서가 없습니다."));

        quotation.setQuotationProgress(Progress.APPROVED);
        return quotationRepository.save(quotation);
    }

    // seller

    // (seller) 신규 견적서 생성 - 받은 견적 요청서와 연관된 견적서를 작성
    public void create(QuotationCreateDto quotationCreateDto) {

        // 유효한 견적 요청 검증 로직
        Optional<QuotationRequest> quotationRequest = quotationRequestRepository.getValidQuotationRequest(quotationCreateDto.getQuotationRequestId());
        if(quotationRequest.isEmpty()) {
            throw new NoSuchElementException("유효한 견적 요청이 없습니다.");
        }

        // 이미지파일 유무 검증 로직
        if(quotationCreateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        Quotation quotation = Quotation.builder()
                .totalTransactionAmount(quotationCreateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest.get())
                .quotationProgress(Progress.PENDING)
                .build();
        Quotation savedQuotation = quotationRepository.save(quotation);

        // 이미지파일 첨부 로직
        imageService.saveMultiImages(quotationCreateDto.getImageFiles(), savedQuotation.getId());
    }

        // (seller) 작성한 견적서 취소처리 => isAvailable = false;
        public Quotation cancelQuotation(Quotation quotation) {
            quotation.setAvailable(false);
        quotation.setQuotationProgress(Progress.SELLER_CANCELLED);
        return quotationRepository.save(quotation);
    }


    // (seller) 견적서 수정(견적서 생성 및 기존 견적서의 유효성 제거) - 견적서를 새로 생성하고 견적요청서와 연관관계를 맺음. 기존의 견적서는 isAvailable = false 처리
    public void updateQuotation(QuotationUpdateDto quotationUpdateDto) {

        // 유효한 견적 요청 검증 로직
        Optional<QuotationRequest> quotationRequest = quotationRequestRepository.getValidQuotationRequest(quotationUpdateDto.getQuotationRequestId());
        if(quotationRequest.isEmpty()) {
            throw new NoSuchElementException("유효한 견적 요청이 없습니다.");
        }

        // 이미지파일 유무 검증 로직
        if(quotationUpdateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        // 기존의 유효한 견적서가 있을 경우 폐기
        Optional<Quotation> formerQuotation = quotationRepository.getValidQuotationForQuotationRequest(quotationUpdateDto.getQuotationRequestId());
        if(formerQuotation.isPresent()){
            formerQuotation.get().setAvailable(false);
            formerQuotation.get().setQuotationProgress(Progress.SELLER_CANCELLED);
        }

        // 수정된 새 견적서 생성
        Quotation newQuotation = Quotation.builder()
                .totalTransactionAmount(quotationUpdateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest.get())
                .quotationProgress(Progress.PENDING)
                .build();


        // DB에 저장
        Quotation saveQuotation = quotationRepository.save(formerQuotation.get());
        quotationRepository.save(newQuotation);

        // 이미지파일 첨부 로직
        imageService.saveMultiImages(quotationUpdateDto.getImageFiles(), saveQuotation.getId());
    }

    // (seller) 고객 결제 전 거래 취소 처리 (견적 요청서에 대해 '거래 불가' 내용이 담긴 견적서 생성. 발송 후 해당 요청서에 대해서 판매자의 견적서 작성 권한 박탈)
    public void makeRejectionContractQuotation(QuotationRequest quotationRequest) {

        // 거래 거철 처리가 가능한 조건: quotationRequest 가 유효해야 하고, 이미 작성한 quotation이 있을 경우 그것의 상태는 사용자 결제 단계 이전이어야 함.

        // 고객이 견적 요청을 취소한 경우
        if(!quotationRequest.isAvailable()) {
            throw new NoSuchElementException("고객의 견적 신청이 유효하지 않습니다.");
        }

        // 견적요청서에 대해서 이미 작성한 유효한 견적서가 있는지 확인
        Optional<Quotation> optionalFormerQuotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId());

        // 이미 생성한 견적서가 있을 경우 -> 조건을 고려해서 폐기
        if(optionalFormerQuotation.isPresent()) {
            Quotation formerQuotation = optionalFormerQuotation.get();

            // 판매지가 이미 견적 취소 처리를 한 경우
            if(formerQuotation.getQuotationProgress().equals(Progress.SELLER_CANCELLED)) {
                throw new NoSuchElementException("이미 취소한 견적입니다.");
            }

            // 최초 견적서 생성 후 고객의 응답 대기중일 경우에만 거래 취소 가능
            if(formerQuotation.getQuotationProgress().equals(Progress.PENDING)
            ) {
                formerQuotation.setAvailable(false);
                formerQuotation.setQuotationProgress(Progress.SELLER_CANCELLED);
            }
            quotationRepository.save(formerQuotation);
            quotationRequestService.invalidateQuotationRequest(quotationRequest); // 고객의 견적 요청의 효력을 상실시킴
            return;
        }

        // 애초에 생성한 견적서가 없을 경우 -> 취소 견적서 생성하여 저장
        Quotation newQuotation = Quotation.builder()
                .quotationRequest(quotationRequest)
                .totalTransactionAmount(null)
                .quotationProgress(Progress.SELLER_CANCELLED)
                .isContractTerminated(true) // 거래 종료 처리
                .build();
        quotationRequest.setAvailable(false);
        quotationRequestRepository.save(quotationRequest);
        quotationRepository.save(newQuotation);
    }

    // (seller) 회사가 작성한 모든 견적서 출력
    public List<Quotation> getAllQuotationOfCompany() {
        Company company = getCompanyOfMember();
        return quotationRepository.getAllQuotationOfCompany(company.getId());
    }


    // admin

    // (admin) (견적서의 유효성 및 삭제 여부 관계없이) 특정 회사가 작성한 모든 견적서 출력
    public List<Quotation> getAllQuotationOfCompanyByAdmin(UUID companyId) {
        return quotationRepository.getAllQuotationOfCompanyByAdmin(companyId);
    }

}
