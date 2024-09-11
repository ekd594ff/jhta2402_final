package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotation.*;
import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final QuotationRequestRepository quotationRequestRepository;
    private final ImageService imageService;


    // 견적서에 접근하는 멤버가 그 견적서와 연관된 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void isCompanyManager(Quotation quotation) {
        Portfolio portfolio = quotation.getQuotationRequest().getPortfolio();
        if (portfolio == null) {
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

    // 이 메서드는 quotation에서 이미지를 로드하여 URL 리스트를 반환
    private List<String> getImageUrlsForQuotation(Quotation quotation) {
        List<Image> images = imageService.getImagesFrom(quotation);
        List<String> imageUrls = new ArrayList<>();
        for (Image image : images) {
            imageUrls.add(image.getUrl());
        }
        return imageUrls;
    }

    // 견적 요청서 작성자 권한

    // (견적요청서를 작성한 사용자 권한) 사용자가 여러 판매자들로부터 받은 모든 견적서 출력
    public Page<QuotationInfoDto> getQuotationInfoDtoListTowardMember(Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("해당 이메일의 사용자를 찾을 수 없습니다."));
        Page<Quotation> quotationsForMember = quotationRepository.GetAllQuotationsTowardMember(member.getId(), pageable);
        Page<QuotationInfoDto> result = quotationsForMember.map(quotation -> {
            List<String> imageUrls = imageService.getImagesFrom(quotation).stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());
            return new QuotationInfoDto(quotation, imageUrls);
        });
        return result;
    }


    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 모든 견적서 출력 (sorted by updatedAt)
    public List<QuotationInfoDto> getQuotationInfoDtoListFrom(QuotationRequest quotationRequest) {
        if (!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        List<Quotation> quotationList = quotationRequest.getQuotations()
                .stream()
                .sorted(Comparator.comparing(Quotation::getUpdatedAt))
                .toList(); // updatedAt으로 정렬

        List<QuotationInfoDto> quotationInfoDtoList = new ArrayList<>();

        for (Quotation quotation : quotationList) {
            List<Image> images = imageService.getImagesFrom(quotation);  // Quotation별로 이미지 로드
            List<String> imageUrls = Optional.ofNullable(images)
                    .map(imgList -> imgList.stream().map(Image::getUrl).toList())
                    .orElse(new ArrayList<>());
            quotationInfoDtoList.add(new QuotationInfoDto(quotation, imageUrls));
        }
        return quotationInfoDtoList;
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 견적서 중 대기중인 견적서(1개) 출력
    public QuotationInfoDto getValidQuotationForQuotationRequest(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING)
                .orElseThrow(() -> new NoSuchElementException("대기중인 견적서가 없습니다."));
        List<String> imageUrls = imageService.getImagesFrom(quotation).stream().map(Image::getUrl).toList();
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 견적서를 받은 이후 거래 취소
    public void cancelQuotationByCustomer(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING)
                .orElseThrow(() -> new NoSuchElementException("대기중인 견적서가 없습니다."));

        quotation.setProgress(QuotationProgress.USER_CANCELLED);
        quotationRepository.save(quotation);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 받은 견적서 승인 조치
    public void approveQuotation(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING)
                .orElseThrow(() -> new NoSuchElementException("대기중인 견적서가 없습니다."));

        quotation.setProgress(QuotationProgress.APPROVED);
        quotationRepository.save(quotation);
    }

    // seller

    // (seller) 신규 견적서 생성 - 받은 견적 요청서와 연관된 견적서를 작성
    public void creatQuotationBySeller(QuotationCreateDto quotationCreateDto) {

        Company company = getCompanyOfMember();

        // 견적 요청 검증 로직 (견적요청이 PENDING인 경우 확인)
        QuotationRequest quotationRequest = quotationRequestRepository.findQuotationRequestByIdAndProgressByCompany(company.getId(), quotationCreateDto.getQuotationRequestId(), QuotationProgress.PENDING)
                .orElseThrow(() -> new NoSuchElementException("대기중인 견적 요청이 없습니다."));

        // 이미지파일 유무 검증 로직
        if(quotationCreateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        Quotation quotation = Quotation.builder()
                .totalTransactionAmount(quotationCreateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest)
                .build();
        Quotation savedQuotation = quotationRepository.save(quotation);

        // 이미지 파일 업로드 및 이미지 객체 저장
        imageService.saveMultiImages(quotationCreateDto.getImageFiles(), savedQuotation.getId());

    }

    // (seller) 선택한 견적서 취소처리
    public void cancelQuotationBySeller(Quotation quotation) {
        if (!quotation.getProgress().equals(QuotationProgress.PENDING)) {
            throw new NoSuchElementException("견적서가 대기중 상태가 아니라 취소 불가");
        }
        quotation.setProgress(QuotationProgress.SELLER_CANCELLED);
        quotationRepository.save(quotation);
    }

    // (seller) 견적서 수정(견적서 생성 및 기존 견적서의 유효성 제거) - 견적서를 새로 생성하고 견적요청서와 연관관계를 맺음. 기존의 견적서는 isAvailable = false 처리
    public void updateQuotation(QuotationUpdateDto quotationUpdateDto) {

        // 견적 요청 검증 로직(대기중인 견적요청만 가능)
        QuotationRequest quotationRequest = quotationRequestRepository.findQuotationRequestById(quotationUpdateDto.getQuotationRequestId())
                .orElseThrow(() -> new NoSuchElementException("견적 요청이 없습니다."));

        // 거래 거철 처리가 가능한 조건: quotationRequest 가 유효해야 하고, 이미 작성한 quotation이 있을 경우 그것의 상태는 사용자 결제 단계 이전이어야 함.

        // 고객이 견적 요청을 취소한 경우
        if(quotationRequest.getProgress().equals(QuotationProgress.USER_CANCELLED)) {
            throw new NoSuchElementException("고객의 견적 신청이 취소되었습니다.");
        }

        // 관리자가 견적 요청을 취소한 경우
        if(quotationRequest.getProgress().equals(QuotationProgress.ADMIN_CANCELLED)) {
            throw new NoSuchElementException("고객의 견적 신청이 유효하지 않습니다.");
        }

        // 고객이 견적 요청을 취소한 경우
        if(quotationRequest.getProgress().equals(QuotationProgress.APPROVED)) {
            throw new NoSuchElementException("고객이 이미 견적을 승인하여 수정이 불가합니다.");
        }

        // 이미지파일 유무 검증 로직
        if(quotationUpdateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        // 기존의 대기중인 견적서가 있을 경우 취소 처리
        Optional<Quotation> formerQuotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationUpdateDto.getQuotationRequestId(), QuotationProgress.PENDING);
        formerQuotation.ifPresent(this::cancelQuotationBySeller);

        // 수정된 새 견적서 생성
        Quotation newQuotation = Quotation.builder()
                .totalTransactionAmount(quotationUpdateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest)
                .build();

        // DB에 저장
        Quotation saveQuotation = quotationRepository.save(formerQuotation.get());
        quotationRepository.save(newQuotation);

        // 이미지 파일 업로드 및 이미지 객체 저장
        imageService.saveMultiImages(quotationUpdateDto.getImageFiles(), saveQuotation.getId());
    }

    @Transactional
    // (seller) 고객 결제 전 거래 취소 처리 (견적 요청서에 대해 '거래 불가' 내용이 담긴 견적서 생성. 발송 후 해당 요청서에 대해서 판매자의 견적서 작성 권한 박탈)
    public void makeCancelQuotationBySeller(QuotationRequest quotationRequest) {

        // 거래 거철 처리가 가능한 조건: quotationRequest 가 대기중
        switch (quotationRequest.getProgress()) {
            case USER_CANCELLED:
                throw new NoSuchElementException("고객의 견적 신청이 취소되었습니다.");
            case SELLER_CANCELLED:
                throw new NoSuchElementException("판매자가 이미 고객의 견적 요청을 거절하였습니다.");
            case ADMIN_CANCELLED:
                throw new NoSuchElementException("관리자 조치: 고객의 견적 신청이 유효하지 않습니다.");
            case APPROVED:
                throw new NoSuchElementException("고객이 견적을 승인하여 취소가 불가합니다.");
            default:
                break;
        }

        // 견적요청서에 대해서 이미 작성한 대기중인 견적서가 있는지 확인
        Optional<Quotation> optionalFormerQuotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING);

        // 이미 판매자가 생성한 견적서가 있을 경우 -> 견적서의 progress를 판매자 취소 처리
        if(optionalFormerQuotation.isPresent()) {
            optionalFormerQuotation.get().setProgress(QuotationProgress.SELLER_CANCELLED);
            quotationRepository.save(optionalFormerQuotation.get());
        }

        // 견적 요청서를 취소 처리하여 저장
        quotationRequest.setProgress(QuotationProgress.SELLER_CANCELLED);
        quotationRequestRepository.save(quotationRequest);
    }

    // (seller) 회사가 작성한 모든 견적서 출력
    public List<Quotation> getAllQuotationOfCompany() {
        Company company = getCompanyOfMember();
        return quotationRepository.findAllByCompany(company.getId());
    }


    // admin

    // (admin) 특정 회사가 작성한 모든 견적서 출력
    public Page<QuotationInfoDto> getAllQuotationOfCompanyByAdmin(Company company, Pageable pageable) {
        Page<Quotation> quotations = quotationRepository.findAllByCompany(company.getId(), pageable);

        // Quotation을 QuotationInfoDto로 매핑
        Page<QuotationInfoDto> result = quotations.map(quotation -> {
            List<String> imageUrls = getImageUrlsForQuotation(quotation);
            return new QuotationInfoDto(quotation, imageUrls);
        });

        return result;
    }

    public Page<QuotationResponseDto> findAllQutationResponseDto(Pageable pageable) {
        return quotationRepository.findAll(pageable).map(QuotationResponseDto::new);
    }

    public Page<QuotationResponseDto> findAllByFilter(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "id" -> {
                    return quotationRepository.findAllByIdContains(filterValue.get(), pageable).map(QuotationResponseDto::new);
                }
                case "totalTransactionAmount" -> {
                    return quotationRepository.findAllByTotalTransactionAmountContains(filterValue.get(), pageable).map(QuotationResponseDto::new);
                }
                case "progress" -> {
                    return quotationRepository.findAllByProgressContains(filterValue.get(), pageable).map(QuotationResponseDto::new);
                }
                case "createdAt" -> {
                    return quotationRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(QuotationResponseDto::new);
                }
                case "updatedAt" -> {
                    return quotationRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(QuotationResponseDto::new);
                }
            }
        } else {
            return quotationRepository.findAll(pageable).map(QuotationResponseDto::new);
        }
        throw new RuntimeException("findAllByFilter : QuotationService");
    }

    public void softDeleteQuotation(List<String> idList) {
//        quotationRepository.
    }

    public void updateProgess(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        quotationRepository.updateQuotationById(ids);
    }


    @Transactional
    public void editQuotationForAdmin(EditQuotationDto editQuotationDto) {
        Optional<Quotation> quotationOptional = quotationRepository.findById(editQuotationDto.getId());
        if (quotationOptional.isPresent()) {
            Quotation quotation = quotationOptional.get();
            quotation.setProgress(editQuotationDto.getProgress());
        } else {
            throw new NoSuchElementException("editQuotationForAdmin");
        }
    }
}
