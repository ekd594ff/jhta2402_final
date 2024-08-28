package com.user.IntArea.repository;

import com.user.IntArea.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT r FROM Review r " +
            "INNER JOIN Quotation q ON q.id = r.quotation.id " +
            "INNER JOIN QuotationRequest qr ON qr.id = q.quotationRequest.id " +
            "WHERE qr.portfolio.id = :portfolioId ")
    Page<Review> findReviewsByPortfolioId(UUID portfolioId, Pageable pageable);
}
