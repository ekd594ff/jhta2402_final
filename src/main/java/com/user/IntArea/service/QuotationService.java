package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.repository.CompanyRepository;
import com.user.IntArea.repository.MemberRepository;
import com.user.IntArea.repository.QuotationRepository;
import com.user.IntArea.repository.QuotationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final QuotationRequestRepository quotationRequestRepository;

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

    //


    // 견적 요청서 작성자 권한

    // (견적요청서를 작성한 사용자 권한) 사용자가 작성한 모든 견적요청서 출력 => QuotationRequestService 로 이동
    public List<QuotationRequest> getAllQuotationRequestOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        return quotationRequestRepository.findAllByMember(member);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 견적서 출력
    public Quotation getValidQuotationForQuotationRequest(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        try{
            Quotation quotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId());
            if(quotation == null) {
                throw new NoSuchElementException("유효한 견적서가 없습니다.");
            }
            return quotation;
        } catch (Exception e) {
            throw new NoSuchElementException("알 수 없는 오류.");
        }
    }

    // seller

    // (seller) 신규 견적서 생성 - 받은 견적 요청서와 연관된 견적서를 작성
    public Quotation create(QuotationCreateDto quotationCreateDto) {
        Quotation quotation = Quotation.builder()
                .totalTransactionAmount(quotationCreateDto.getTotalTransactionAmount())
                .quotationRequest(quotationCreateDto.getQuotationRequestId())
                .build();
        return quotationRepository.save(quotation);
    }

    // (seller) 작성한 견적서 취소처리
    public Quotation cancelQuotation(Quotation quotation) {
        quotation.setAvailable(false);
        quotation.setProgressStatus(QuotationProgress.SELLER_CANCELLED);
        return quotationRepository.save(quotation);
    }


    // (seller) 견적서 수정(견적서 생성 및 기존 견적서의 유효성 제거) - 견적서를 새로 생성하고 견적요청서와 연관관계를 맺음. 기존의 견적서는 isAvailable = false 처리
    public void updateQuotation(Quotation modifiedQuotation) {

        // 기존의 견적서 폐기
        Quotation formerQuotation = quotationRepository.getValidQuotationForQuotationRequest(modifiedQuotation.getQuotationRequest().getId());
        formerQuotation.setAvailable(false);
        formerQuotation.setProgressStatus(QuotationProgress.SELLER_UPDATED_TO_NEW_QUOTATION);

        // 새 견적서 생성
        Quotation newQuotation = Quotation.builder()
                .totalTransactionAmount(modifiedQuotation.getTotalTransactionAmount())
                .quotationRequest(modifiedQuotation.getQuotationRequest())
                .build();


        quotationRepository.save(formerQuotation);
        quotationRepository.save(newQuotation);

    }

    // (seller) 거래 불가 처리 (견적 요청서에 대해 '견적 불가' 내용이 담긴 견적서 생성. 발송 후 해당 요청서에 대해서 판매자의 견적서 작성 권한 박탈)
    public Quotation makeRejectContractQuotation(QuotationRequest quotationRequest) {

        // 기존의 견적서가 있을 경우 폐기
        Quotation formerQuotation = quotationRepository.getValidQuotationForQuotationRequest(quotationRequest.getId());
        if(formerQuotation != null) {
            formerQuotation.setAvailable(false);
            formerQuotation.setProgressStatus(QuotationProgress.SELLER_CANCELLED);
        }

        Quotation newQuotation = Quotation.builder()
                .quotationRequest(quotationRequest)
                .totalTransactionAmount(0L)
                .progressStatus(QuotationProgress.SELLER_CANCELLED)
                .isContractTerminated(true)
                .build();
        // (TODO) 고객의 quotationRequest 의 유효성을 제거하는 매서드를 추가해야 함.
        return quotationRepository.save(newQuotation);
    }


    // (seller) 회사 관리자가 받은 모든 견적 요청서 출력 => (TODO) QuotationRequestService에 작성해야 함.
    public List<QuotationRequest> getAllQuotationRequestTowardCompany() {
        Company company = getCompanyOfMember();
        return quotationRequestRepository.getAllQuotationRequestTowardCompany(company.getId());
    }

    // (seller) 회사 관리자가 작성한 모든 견적서 출력
    public List<Quotation> getAllQuotationOfCompany() {
        Company company = getCompanyOfMember();
        return quotationRepository.getAllQuotationOfCompany(company.getId());
    }

    // admin

    // (admin) 특정 회사가 받은 모든 견적 요청서 출력 => QuotationRequestService에서 작성
    // (admin) 특정 회사가 작성한 모든 견적서 출력

}
