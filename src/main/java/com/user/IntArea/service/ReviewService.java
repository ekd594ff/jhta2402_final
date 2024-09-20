package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.review.*;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final QuotationRepository quotationRepository;
    private final CompanyRepository companyRepository;
    private final QuotationRequestRepository quotationRequestRepository;


    private Member loggedMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인해주세요."));
        String email = memberDto.getEmail();
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 사용자 이메일에 대한 정보가 없습니다."));
    }

    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException("판매자 권한이 없습니다."));
        return company;
    }

    // 현재 로그인한 멤버가 해당 견적서에 대한 견적요청서의 작성 권한자인지 확인
    private void checkIfLoggedMemberCanWriteReviewFor(Quotation quotation) {
        Member member = loggedMember();
        QuotationRequest quotationRequest = quotation.getQuotationRequest();
        if (quotationRequest == null) {
            throw new NoSuchElementException("작성된 견적 요청서가 없습니다");
        }
        if (!member.equals(quotationRequest.getMember())) {
            throw new NoSuchElementException("리뷰 작성 권한이 없습니다.");
        }
        if (!quotationRequest.getProgress().equals(QuotationProgress.APPROVED) || !quotation.getProgress().equals(QuotationProgress.APPROVED)) {
            throw new NoSuchElementException("거래가 승인된 상태에서만 리뷰 작성이 가능합니다.");
        }
        Optional<Review> review = reviewRepository.findByQuotationId(quotation.getId());
        if (review.isPresent()) {
            throw new NoSuchElementException("이미 리뷰를 작성하였습니다.");
        }
    }

    // (APPROVED QuotationRequest를 가진 사용자 권한) 현재 로그인한 멤버가 해당 리뷰에 대한 수정 권한자인지 확인
    private void checkLoggedMemberAndGetReviewFor(Review review) {
        Member member = loggedMember();
        Quotation quotation = review.getQuotation();
        QuotationRequest quotationRequest = quotation.getQuotationRequest();
        if (quotationRequest == null) {
            throw new NoSuchElementException("알 수 없는 오류. 작성된 견적 요청서가 없습니다");
        }
        if (!member.equals(quotationRequest.getMember())) {
            throw new NoSuchElementException("리뷰 작성 권한이 없습니다.");
        }
        if (!member.getId().equals(review.getMember().getId())) {
            throw new NoSuchElementException("리뷰가 관리자에 의해 조치되어 수정이 불가합니다.");
        }
        if (!quotationRequest.getProgress().equals(QuotationProgress.APPROVED) || !quotation.getProgress().equals(QuotationProgress.APPROVED)) {
            throw new NoSuchElementException("거래가 승인된 상태에서만 리뷰 작성이 가능합니다.");
        }
    }

    // (APPROVED QuotationRequest를 가진 사용자 권한) 사용자의 리뷰 작성
    @Transactional
    public ReviewResponseDto create(CreateReviewDto createReviewDto, UUID quotationId) {
        Quotation quotation = quotationRepository.getByQuotationId(quotationId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 견적서가 존재하지 않습니다."));

        // 권한 확인
        checkIfLoggedMemberCanWriteReviewFor(quotation);
        Review review = Review.builder()
                .createReviewDto(createReviewDto)
                .quotation(quotation)
                .member(loggedMember())
                .build();
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .review(savedReview)
                .portfolioId(quotation.getQuotationRequest().getId())
                .build();
    }

    @Transactional
    public ReviewResponseDto createByQuotationRequestId(CreateReviewDto createReviewDto, UUID quotationRequestId) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(quotationRequestId)
                .orElseThrow(() -> new NoSuchElementException("해당 견적신청서가 없습니다."));

        UUID quotationId = quotationRequest.getQuotations().stream()
                .filter(request -> request.getProgress().equals(QuotationProgress.APPROVED)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("완료상태인 견적서가 없습니다.")).getId();

        return create(createReviewDto, quotationId);
    }

    // (리뷰 작성자 권한) 리뷰 수정
    @Transactional
    public void updateReviewByWriter(UpdateReviewDto updateReviewDto) {
        UUID reviewId = updateReviewDto.getId();
        Review review = reviewRepository.getByReviewId(reviewId)
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 오류. 리뷰가 존재하지 않습니다."));

        // 수정권한 확인
        checkLoggedMemberAndGetReviewFor(review);
        if (updateReviewDto.getTitle().isBlank() || updateReviewDto.getDescription().isBlank()) {
            throw new NoSuchElementException("제목과 내용에 모두 글을 작성해주세요.");
        }
        review.setTitle(updateReviewDto.getTitle());
        review.setDescription(updateReviewDto.getDescription());
        review.setRate(updateReviewDto.getRate());
        reviewRepository.save(review);
    }

    // (일반) 사용자가 작성한 모든 리뷰 리스트 출력
    public Page<ReviewInfoListDto> getReviewInfoDtoListOfMember(Pageable pageable) {
        Member member = loggedMember();
        return reviewRepository.getAllReviewsByMember(member.getId(), pageable)
                .map(ReviewInfoListDto::new);
    }

    // (일반 권한) 특정한 회사가 받은 모든 리뷰 리스트 출력
    public Page<ReviewInfoListDto> getReviewInfoDtoListTowardCompanyIdForUser(UUID companyId, Pageable pageable) {
        return reviewRepository.getAllReviewsSortedByCompanyForUser(companyId, pageable)
                .map(ReviewInfoListDto::new);
    }

    // (seller 권한) 회사가 받은 모든 리뷰 리스트 출력
    public Page<ReviewInfoListDto> getReviewInfoDtoListTowardCompany(Pageable pageable) {
        Company company = getCompanyOfMember();
        return reviewRepository.getAllReviewsSortedByCompany(company.getId(), pageable)
                .map(ReviewInfoListDto::new);
    }

    // (일반) 포트폴리오에 달린 모든 리뷰 정보 출력
    public Page<ReviewPortfolioDetailDto> getReviewByPortfolioId(UUID portfolioId, Pageable pageable) {
        return reviewRepository.findReviewsByPortfolioId(portfolioId, pageable)
                .map(ReviewPortfolioDetailDto::new);
    }

    public Page<ReviewPortfolioDto> getAllReview(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(ReviewPortfolioDto::new);
    }

    public Page<ReviewPortfolioDto> getAllSearchReview(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "title" -> {
                    return reviewRepository.findAllByTitleContains(filterValue.get(), pageable).map(ReviewPortfolioDto::new);
                }
                case "description" -> {
                    return reviewRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(ReviewPortfolioDto::new);
                }
                case "rate" -> {
                    return reviewRepository.findAllByRate(Double.parseDouble(filterValue.get()), pageable).map(ReviewPortfolioDto::new);
                }
                case "createdAt" -> {
                    return reviewRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(ReviewPortfolioDto::new);
                }
                case "updatedAt" -> {
                    return reviewRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(ReviewPortfolioDto::new);
                }
                case "username" -> {
                    return reviewRepository.findAllByUsernameContains(filterValue.get(), pageable).map(ReviewPortfolioDto::new);
                }
            }
        } else {
            return reviewRepository.findAll(pageable).map(ReviewPortfolioDto::new);
        }
        throw new RuntimeException("getSearchPortfolio");
    }

    @Transactional
    public void hardDeleteReviews(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        reviewRepository.deleteAllById(ids);
    }

    @Transactional
    public void editReviewForAdmin(EditReviewDto editReviewDto) {
        Optional<Review> reviewOptional = reviewRepository.findById(editReviewDto.getId());
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setTitle(editReviewDto.getTitle());
            review.setDescription(editReviewDto.getDescription());
        } else {
            throw new NoSuchElementException("editReviewForAdmin");
        }
    }
}
