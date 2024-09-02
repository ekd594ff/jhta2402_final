package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
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
    public ResponseEntity<Page<ReviewPortfolioDetailDto>> getAllReview(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewPortfolioDetailDto> reviewPortfolioDetailDtoPage = reviewService.getAllReview(pageable);
        return ResponseEntity.ok().body(reviewPortfolioDetailDtoPage);
    }
}
