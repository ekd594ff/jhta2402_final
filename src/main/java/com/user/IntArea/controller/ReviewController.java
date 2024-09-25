package com.user.IntArea.controller;

import com.user.IntArea.dto.review.*;
import com.user.IntArea.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 일반 권한
    // 견적신청서 ID로 리뷰 작성
    @PostMapping("/quotationRequest/{quotationRequestId}")
    public ResponseEntity<ReviewResponseDto> createReviewByQuotationRequestId(@RequestBody CreateReviewDto createReviewDto, @PathVariable UUID quotationRequestId) {
        ReviewResponseDto reviewResponseDto = reviewService.createByQuotationRequestId(createReviewDto, quotationRequestId);
        return ResponseEntity.ok(reviewResponseDto);
    }

    // (승인된 quotationRequest 작성자 권한) 리뷰 작성(새로 만들기)
    @PostMapping("/{quotationId}") // ● postman pass
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody CreateReviewDto createReviewDto, @PathVariable UUID quotationId) {
        ReviewResponseDto reviewResponseDto = reviewService.create(createReviewDto, quotationId);
        return ResponseEntity.ok(reviewResponseDto);
    }

    // (승인된 quotationRequest 작성자 권한) 작성된 리뷰 수정하기
    @PostMapping("/update") // ● postman pass
    public ResponseEntity<?> updateReviewByWriter(@RequestBody UpdateReviewDto updateReviewDto) {
        reviewService.updateReviewByWriter(updateReviewDto);
        return ResponseEntity.ok().build();
    }

    // (일반 사용자 권한) 사용자가 작성한 모든 리뷰 정보 조회
    @GetMapping("/list") // ● postman pass
    public Page<ReviewInfoListDto> getAllReviewsOfUser(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return reviewService.getReviewInfoDtoListOfMember(pageable);
    }

    // (일반) 하나의 포트폴리오에 딸린 여러개의 리뷰 조회하기 (?)
    @GetMapping("/portfolio/{id}")
    public ResponseEntity<Page<ReviewPortfolioDetailDto>> getReviewByPortfolioId(
            @PathVariable UUID id, @RequestParam int page, @RequestParam int size, @RequestParam String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        Page<ReviewPortfolioDetailDto> portfolioDetailDtos = reviewService.getReviewByPortfolioId(id, pageable);

        return ResponseEntity.ok().body(portfolioDetailDtos);
    }

    // (일반) 특정한 회사가 받은 모든 리뷰 조회(isApplied = true 인 회사만 조회)
    @GetMapping("/list/company/{companyId}") // ● postman pass
    public Page<ReviewInfoListDto> getAllReviewsByCompanyId(@PathVariable UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return reviewService.getReviewInfoDtoListTowardCompanyIdForUser(companyId, pageable);
    }
    
    // seller 권한

    // (seller 권한) 회사가 받은 모든 리뷰 조회
    @GetMapping("/company/list") // ● postman pass
    public Page<ReviewInfoListDto> getAllReviewsForCompany(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return reviewService.getReviewInfoDtoListTowardCompany(pageable);
    }

    // admin 권한

    @GetMapping("/admin/list")
    public ResponseEntity<Page<ReviewPortfolioDto>> getAllReview(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        Page<ReviewPortfolioDto> reviewPortfolioDetailDtoPage = reviewService.getAllReview(pageable);
        return ResponseEntity.ok().body(reviewPortfolioDetailDtoPage);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<ReviewPortfolioDto>> getSearchReview(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                          @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                          @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                          @RequestParam(required = false) String filterColumn,
                                                                          @RequestParam(required = false) String filterValue) {
        if (sortField.equals("username")) {
            sortField = "member.username";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<ReviewPortfolioDto> reviewPortfolioDetailDtoPage = reviewService.getAllSearchReview(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue), pageable);
        return ResponseEntity.ok().body(reviewPortfolioDetailDtoPage);
    }

    @DeleteMapping("/admin/{ids}")
    public ResponseEntity<?> deleteReviews(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        reviewService.hardDeleteReviews(idList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> editReview(@RequestBody EditReviewDto editReviewDto) {
        reviewService.editReviewForAdmin(editReviewDto);
        return ResponseEntity.noContent().build();
    }
}
