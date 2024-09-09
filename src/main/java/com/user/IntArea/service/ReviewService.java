package com.user.IntArea.service;

import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public Page<ReviewPortfolioDetailDto> getAllSearchReview(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "user" -> {
                    return reviewRepository.findAllByUsernameContains(filterValue.get(), pageable).map(ReviewPortfolioDetailDto::new);
                }
                case "title" -> {
                    return reviewRepository.findAllByTitleContains(filterValue.get(), pageable).map(ReviewPortfolioDetailDto ::new);
                }
                case "description" -> {
                    return reviewRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(ReviewPortfolioDetailDto ::new);
                }
                case "rate" -> {
                    return reviewRepository.findAllByRate(Double.parseDouble(filterValue.get()), pageable).map(ReviewPortfolioDetailDto::new);
                }
                case "createdAt" -> {
                    return reviewRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(ReviewPortfolioDetailDto::new);
                }
                case "updatedAt" -> {
                    return reviewRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(ReviewPortfolioDetailDto::new);
                }
                case "username" -> {
                    return reviewRepository.findAllByUsernameContains(filterColumn.get(), pageable).map(ReviewPortfolioDetailDto::new);
                }
            }
        } else {
            return reviewRepository.findAll(pageable).map(ReviewPortfolioDetailDto::new);
        }
        throw new RuntimeException("getSearchPortfolio");
    }

    @Transactional
    public void hardDeleteReviews(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        reviewRepository.deleteAllById(ids);
    }
}
