package com.user.IntArea.service;

import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.review.EditReviewDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.dto.review.ReviewPortfolioDto;
import com.user.IntArea.entity.Review;
import com.user.IntArea.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

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
                    return reviewRepository.findAllByTitleContains(filterValue.get(), pageable).map(ReviewPortfolioDto ::new);
                }
                case "description" -> {
                    return reviewRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(ReviewPortfolioDto ::new);
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
