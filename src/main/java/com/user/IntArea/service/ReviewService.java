package com.user.IntArea.service;

import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Page<ReviewPortfolioDetailDto> getReviewByPortfolioId(UUID portfolioId, Pageable pageable) {
        return reviewRepository.findReviewsByPortfolioId(portfolioId, pageable)
                .map(ReviewPortfolioDetailDto::new);
    }

    public Page<ReviewPortfolioDetailDto> getAllReview(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(ReviewPortfolioDetailDto::new);
    }
}
