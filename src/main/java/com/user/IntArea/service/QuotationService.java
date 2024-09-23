package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotation.*;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 권한 확인 및 유틸

    // 견적서에 접근하는 멤버가 그 견적서와 연관된 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void checkIsCompanyManagerOf(Quotation quotation) {
        Portfolio portfolio = quotation.getQuotationRequest().getPortfolio();
        if (portfolio == null) {
            throw new NoSuchElementException("알 수 없는 오류. 포트폴리오가 존재하지 않습니다.");
        }
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일 정보가 없습니다."));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException("판매자 권한이 없습니다."));
        if (!company.equals(portfolio.getCompany())) {
            throw new NoSuchElementException("권한이 없습니다.");
        }
    }

    // 현재 로그인한 사용자 확인
    private Member getLoggedMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일 정보가 없습니다."));
    }

    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일 정보가 없습니다."));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException("판매자 권한이 없습니다."));
        return company;
    }

    // 현재 로그인한 멤버가 해당 견적요청서의 작성자인지 확인
    private void checkIfLoggedMemberIsQuotationRequestWriter(QuotationRequest quotationRequest) {
        MemberDto memberDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new NoSuchElementException("로그인 정보가 없습니다."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 이메일을 가진 멤버가 없습니다."));
        if (!member.equals(quotationRequest.getMember())) {
            throw new NoSuchElementException("접근 권한이 없습니다.");
        }
    }

    // Quotation을 QuotationInfoDto로 변환하는 메서드
    private QuotationInfoDto convertToQuotationInfoDto(Quotation quotation) {
        List<String> imageUrls = new ArrayList<>();
        try { // 견적서와 관련된 이미지 로드
            List<Image> images = imageService.getImagesFrom(quotation);
            if (images != null) {
                imageUrls = images.stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) { // 예외 발생 시 로그 남기기
            System.err.println("이미지 로드 실패: " + e.getMessage());
        }
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // 진행상태가 PENDING인지 확인
    private void checkIfProgressIsPending(QuotationProgress progress) {
        switch (progress) {
            case USER_CANCELLED:
                throw new NoSuchElementException("고객의 견적 신청이 취소되었습니다.");
            case SELLER_CANCELLED:
                throw new NoSuchElementException("판매자가 고객의 견적 요청을 거절하였습니다.");
            case ADMIN_CANCELLED:
                throw new NoSuchElementException("관리자 조치: 고객의 견적 신청이 유효하지 않습니다.");
            case APPROVED:
                throw new NoSuchElementException("고객이 이미 견적을 승인하여 취소가 불가합니다.");
            case PENDING:
                break;
            default:
                throw new NoSuchElementException("알 수 없는 오류. 견적서의 진행상태에 문제가 있습니다.");
        }
    }

    // 일반 권한

    // (일반) 특정 견적서 출력
    public Quotation getById(UUID quotationId) {
        Optional<Quotation> quotation = quotationRepository.findById(quotationId);
        if (quotation.isEmpty()) {
            throw new NoSuchElementException("작성된 견적서가 없습니다.");
        }
        return quotation.get();
    }

    // 견적 요청서 작성자 권한

    // (견적요청서를 작성한 사용자 권한) 사용자가 여러 판매자들로부터 받은 모든 견적서 출력
    public Page<QuotationInfoDto> getQuotationInfoDtoListTowardMember(Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("해당 이메일의 사용자를 찾을 수 없습니다."));
        Page<Quotation> quotationsForMember = quotationRepository.GetAllByMemberId(member.getId(), pageable);

        return quotationsForMember.map(this::convertToQuotationInfoDto);
    }

    // (견적서를 받은 사용자 권한) 사용자가 판매자로부터 받은 하나의 견적서 정보 열람
    public QuotationInfoDto getQuotationInfoDtoByMember(UUID quotationId) {
        Member member = getLoggedMember();
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 없습니다."));
        if (!quotation.getQuotationRequest().getMember().equals(member)) {
            throw new NoSuchElementException("열람 권한이 없습니다.");
        }
        List<Image> images = imageService.getImagesFrom(quotation);
        List<String> imageUrls = images.stream().map(Image::getUrl).collect(Collectors.toList());
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 여러 판매자들로부터 받은 모든 견적서 출력 (progress에 따라 소팅)
    public Page<QuotationInfoDto> getQuotationInfoDtoListTowardMemberSortedByProgress(QuotationProgress progress, Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요"));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("해당 이메일의 사용자를 찾을 수 없습니다."));
        Page<Quotation> quotationsForMember = quotationRepository.GetAllByMemberIdAndProgress(member.getId(), progress, pageable);

        // Quotation을 QuotationInfoDto로 변환하여 반환
        return quotationsForMember.map(this::convertToQuotationInfoDto);
    }


    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 모든 견적서 출력 (sorted by updatedAt)
    public List<QuotationInfoDto> getQuotationInfoDtoListFrom(QuotationRequest quotationRequest) {
        checkIfLoggedMemberIsQuotationRequestWriter(quotationRequest);
        List<Quotation> quotationList = quotationRequest.getQuotations()
                .stream()
                .sorted(Comparator.comparing(Quotation::getUpdatedAt))
                .toList(); // updatedAt으로 정렬

        List<QuotationInfoDto> quotationInfoDtoList = new ArrayList<>();
        for (Quotation quotation : quotationList) {
            quotationInfoDtoList.add(convertToQuotationInfoDto(quotation));
        }
        return quotationInfoDtoList;
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 견적서 중 대기중인 견적서(1개) 출력
    public QuotationInfoDto getValidQuotationForQuotationRequest(QuotationRequest quotationRequest) {
        checkIfLoggedMemberIsQuotationRequestWriter(quotationRequest);
        Quotation quotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING)
                .orElseThrow(() -> new NoSuchElementException("대기중인 견적서가 없습니다."));
        List<String> imageUrls = imageService.getImagesFrom(quotation).stream().map(Image::getUrl).toList();
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 받은 견적서만 취소
    @Transactional
    public void cancelQuotationByCustomer(Quotation quotation) {
        checkIfLoggedMemberIsQuotationRequestWriter(quotation.getQuotationRequest());
        // 견적서 취소 가능 조건: 견적서의 진행상태가 PENDING
        checkIfProgressIsPending(quotation.getProgress());

        quotation.setProgress(QuotationProgress.USER_CANCELLED);
        quotationRepository.save(quotation);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 견적서를 받은 이후 거래를 완전 취소
    @Transactional
    public void cancelContractByCustomer(UUID quotationId) {
        Quotation quotation = quotationRepository.getByQuotationId(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 없습니다."));
        QuotationRequest quotationRequest = quotation.getQuotationRequest();

        checkIfLoggedMemberIsQuotationRequestWriter(quotationRequest);
        checkIfProgressIsPending(quotation.getProgress());
        checkIfProgressIsPending(quotationRequest.getProgress());

        quotation.setProgress(QuotationProgress.USER_CANCELLED);
        quotationRepository.save(quotation);
        quotationRequest.setProgress(QuotationProgress.USER_CANCELLED);
        quotationRequestRepository.save(quotationRequest);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 받은 견적서 승인 조치
    @Transactional
    public void approveQuotationByCustomer(UUID quotationId) {
        Quotation quotation = quotationRepository.getByQuotationId(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 없습니다."));
        QuotationRequest quotationRequest = quotation.getQuotationRequest();

        checkIfLoggedMemberIsQuotationRequestWriter(quotationRequest);
        checkIfProgressIsPending(quotation.getProgress());
        checkIfProgressIsPending(quotationRequest.getProgress());

        quotation.setProgress(QuotationProgress.APPROVED);
        quotationRepository.save(quotation);
        quotationRequest.setProgress(QuotationProgress.APPROVED);
        quotationRequestRepository.save(quotationRequest);
    }

    // seller

    // (seller) 신규 견적서 생성 - 받은 견적 요청서와 연관된 견적서를 작성
    @Transactional
    public void createQuotationBySeller(QuotationCreateDto quotationCreateDto) {
        Company company = getCompanyOfMember();

        // 견적 요청서 검증 로직 (견적요청이 PENDING인 경우 확인)
        List<QuotationRequest> quotationRequestList = quotationRequestRepository.findQuotationRequestByIdAndProgressByCompany(company.getId(), quotationCreateDto.getQuotationRequestId(), QuotationProgress.PENDING);
        QuotationRequest quotationRequest;
        if (quotationRequestList.isEmpty()) {
            throw new NoSuchElementException("대기중인 견적 요청이 없습니다.");
        }
        if (quotationRequestList.size() >= 2) {
            throw new NoSuchElementException("알 수 없는 오류. 대기중인 견적 요청이 2개 이상입니다.");
        }
        quotationRequest = quotationRequestList.get(0);

        // 이미지파일 유무 검증 로직
        if (quotationCreateDto.getImageFiles() == null || quotationCreateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        // 기존 PENDING 상태 QuotationRequest SELLER_CANCELLED로 변경
        quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING)
                .ifPresent(pendingQuotation -> {
                    pendingQuotation.setProgress(QuotationProgress.SELLER_CANCELLED);
                    quotationRepository.save(pendingQuotation);
                });

        Quotation quotation = Quotation.builder()
                .totalTransactionAmount(quotationCreateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest)
                .build();
        Quotation savedQuotation = quotationRepository.save(quotation);

        // 이미지 파일 업로드 및 이미지 객체 저장
        imageService.saveMultiImages(quotationCreateDto.getImageFiles(), savedQuotation.getId());
    }

    // (seller) 선택한 견적서 취소처리
    @Transactional
    public void cancelQuotationBySeller(UUID id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 견적서가 없습니다."));

        // 작성권한자 확인
        checkIsCompanyManagerOf(quotation);

        // 취소 가능 조건
        if (!quotation.getProgress().equals(QuotationProgress.PENDING)) {
            throw new NoSuchElementException("견적서가 대기중 상태가 아니라 취소 불가합니다.");
        }
        quotation.setProgress(QuotationProgress.SELLER_CANCELLED);
        quotationRepository.save(quotation);
    }

    // (seller) 견적서 수정(견적서 새로 생성 및 기존 견적서가 있을 경우 그 유효성 제거) - 견적요청서와 연관관계를 맺은 견적서를 새로 생성,기존의 견적서는 취소처리
    @Transactional
    public void updateQuotationBySeller(QuotationUpdateDto quotationUpdateDto) {
        // 작성권한 확인
        Company company = getCompanyOfMember();

        // 견적 요청서 검증 로직 (견적요청이 PENDING인 경우 확인)
        List<QuotationRequest> quotationRequestList = quotationRequestRepository.findQuotationRequestByIdAndProgressByCompany(company.getId(), quotationUpdateDto.getQuotationRequestId(), QuotationProgress.PENDING);
        QuotationRequest quotationRequest;
        if (quotationRequestList.isEmpty()) {
            throw new NoSuchElementException("판매자가 받은 대기중인 견적 요청이 없습니다.");
        }
        if (quotationRequestList.size() >= 2) {
            throw new NoSuchElementException("알 수 없는 오류. 같은 아이디를 가진 견적요청이 2개 이상입니다.");
        }
        quotationRequest = quotationRequestList.get(0);

        // 이미지파일 유무 검증 로직
        if (quotationUpdateDto.getImageFiles() == null || quotationUpdateDto.getImageFiles().isEmpty()) {
            throw new NoSuchElementException("이미지 파일을 첨부해주세요.");
        }

        // 기존의 대기중인 견적서가 있을 경우 취소 처리 및 저장
        List<Quotation> formerQuotationList = quotationRepository.getAllListByQuotationRequestIdAndProgress(quotationUpdateDto.getQuotationRequestId(), QuotationProgress.PENDING);
        if (formerQuotationList.size() >= 2) {
            throw new NoSuchElementException("알 수 없는 오류. 해당 견적요청서에 대해 작성된 대기중 견적서가 2개 이상입니다.");
        }
        if (formerQuotationList.size() == 1) {
            cancelQuotationBySeller(formerQuotationList.get(0).getId());
            quotationRepository.save(formerQuotationList.get(0));
        }

        // 수정된 새 견적서 생성 및 저장
        Quotation newQuotation = Quotation.builder()
                .totalTransactionAmount(quotationUpdateDto.getTotalTransactionAmount())
                .quotationRequest(quotationRequest)
                .build();
        quotationRepository.save(newQuotation);

        // 새 견적서의 이미지 파일 업로드 및 이미지 객체 저장
        imageService.saveMultiImages(quotationUpdateDto.getImageFiles(), newQuotation.getId());
    }

    @Transactional
    // (seller) 고객 결제 전 거래 취소 처리 (견적 요청서에 대해 '거래 불가' 내용이 담긴 견적서 생성. 발송 후 해당 요청서에 대해서 판매자의 견적서 작성 권한 박탈)
    public void cancelQuotationAndQuotationRequestBySeller(UUID quotationId) {
        // Seller가 quotationRequest 에 대해 접근권한이 있는지 확인
        Company company = getCompanyOfMember();
        QuotationRequest quotationRequest = quotationRequestRepository.findQuotationRequestByQuotationIdAndCompany(company.getId(), quotationId);
        if (quotationRequest == null) {
            throw new NoSuchElementException("견적요청에 대한 접근 권한이 없습니다.");
        }

        // 거래 거절 처리가 가능한 조건: quotationRequest 가 대기중
        checkIfProgressIsPending(quotationRequest.getProgress());

        // 견적요청서에 대해서 이미 작성한 대기중인 견적서가 있는지 확인
        Optional<Quotation> optionalFormerQuotation = quotationRepository.findByQuotationRequestIdAndProgress(quotationRequest.getId(), QuotationProgress.PENDING);

        // 이미 판매자가 생성한 견적서가 있을 경우 -> 견적서의 progress를 판매자 취소 처리
        if (optionalFormerQuotation.isPresent()) {
            optionalFormerQuotation.get().setProgress(QuotationProgress.SELLER_CANCELLED);
            quotationRepository.save(optionalFormerQuotation.get());
        }

        // 견적 요청서를 취소 처리하여 저장
        quotationRequest.setProgress(QuotationProgress.SELLER_CANCELLED);
        quotationRequestRepository.save(quotationRequest);
    }

    // (seller) 작성한 견적서 1개의 정보 열람
    public QuotationInfoDto getQuotationInfoDtoByCompany(UUID quotationId) {
        Company company = getCompanyOfMember();
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 없습니다."));
        if (!quotation.getQuotationRequest().getPortfolio().getCompany().equals(company)) {
            throw new NoSuchElementException("열람 권한이 없습니다.");
        }
        List<Image> images = imageService.getImagesFrom(quotation);
        List<String> imageUrls = images.stream().map(Image::getUrl).collect(Collectors.toList());
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // (seller) 회사가 작성한 모든 견적서 출력
    public Page<QuotationInfoDto> getAllQuotationsOfCompany(Pageable pageable) {
        // 접근 권한 확인
        Company company = getCompanyOfMember();
        Page<Quotation> quotations = quotationRepository.getAllByCompany(company.getId(), pageable);

        // Quotation을 QuotationInfoDto로 변환하여 Page로 반환
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // (seller) 회사가 작성한 특정 진행상태의 모든 견적서 출력 (progress로 소팅)
    public Page<QuotationInfoDto> getAllQuotationsOfCompanySortedByProgress(QuotationProgress progress, Pageable pageable) {
        Company company = getCompanyOfMember();
        Page<Quotation> quotations = quotationRepository.getAllByCompanySortedByProgress(progress, company.getId(), pageable);
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // (seller) 회사가 작성한 Quotation 중 검색어를 이용하여 찾기
    public Page<QuotationInfoDto> getAllQuotationsOfCompanyBySearchword(String searchWord, Pageable pageable) {
        Company company = getCompanyOfMember();
        Page<Quotation> quotations = quotationRepository.getAllByCompanyWithSearchWord(searchWord, company.getId(), pageable);
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // admin

    // (admin) 특정한 하나의 견적서 정보 출력
    public QuotationInfoDto getQuotationInfoDtoByAdmin(UUID quotationId) {
        Member member = getLoggedMember();
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 없습니다."));
        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new NoSuchElementException("열람 권한이 없습니다.");
        }
        List<Image> images = imageService.getImagesFrom(quotation);
        List<String> imageUrls = images.stream().map(Image::getUrl).collect(Collectors.toList());
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // (admin) 특정 회사가 작성한 모든 견적서 출력
    public Page<QuotationInfoDto> getAllQuotationsOfCompanyByAdmin(UUID companyId, Pageable pageable) {
        Page<Quotation> quotations = quotationRepository.getAllByCompany(companyId, pageable);
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // (admin) 특정 회사가 작성한 특정 진행상태의 모든 견적서 출력 (progress로 소팅)
    public Page<QuotationInfoDto> getAllQuotationsOfCompanySortedByProgressByAdmin(UUID companyId, QuotationProgress progress, Pageable pageable) {
        Page<Quotation> quotations = quotationRepository.getAllByCompanySortedByProgress(progress, companyId, pageable);
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // (admin) 모든 견적서 출력
    public Page<QuotationInfoDto> getAllQuotationsByAdmin(Pageable pageable) {
        Page<Quotation> quotations = quotationRepository.findAll(pageable);
        return quotations.map(this::convertToQuotationInfoDto);
    }

    // (admin) 모든 견적서 출력 (progress로 소팅)
    public Page<QuotationInfoDto> getAllQuotationsByProgressByAdmin(QuotationProgress progress, Pageable pageable) {
        Page<Quotation> quotations = quotationRepository.findAllByProgress(progress, pageable);
        return quotations.map(this::convertToQuotationInfoDto);
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

    @Transactional
    public void updateProgress(List<String> idList) {
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
