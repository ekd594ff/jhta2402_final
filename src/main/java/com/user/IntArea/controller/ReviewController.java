package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.portfolio.EditPortfolioDto;
import com.user.IntArea.dto.review.EditReviewDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.dto.review.ReviewPortfolioDto;
import com.user.IntArea.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<Page<ReviewPortfolioDetailDto>> getReviewByPortfolioId(
            @PathVariable UUID id, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ReviewPortfolioDetailDto> portfolioDetailDtos = reviewService.getReviewByPortfolioId(id, pageable);

        return ResponseEntity.ok().body(portfolioDetailDtos);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<ReviewPortfolioDto>> getAllReview(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewPortfolioDto> reviewPortfolioDetailDtoPage = reviewService.getAllReview(pageable);
        return ResponseEntity.ok().body(reviewPortfolioDetailDtoPage);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<ReviewPortfolioDto>> getSearchReview(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                          @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                          @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                          @RequestParam(required = false) String filterColumn,
                                                                          @RequestParam(required = false) String filterValue) {
        log.info("sortField={}", sortField);
        log.info("sort={}", sort);
        log.info("filterColumn={}", filterColumn);
        log.info("filterValue={}", filterValue);

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
