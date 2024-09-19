package com.user.IntArea.repository;

import com.user.IntArea.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT r from Review r where r.id = :reviewId")
    Optional<Review> getByReviewId(UUID reviewId);

    Optional<Review> findByQuotationId(UUID quotationId);

    @Query("SELECT r FROM Review r " +
            "INNER JOIN Quotation q ON q.id = r.quotation.id " +
            "INNER JOIN QuotationRequest qr ON qr.id = q.quotationRequest.id " +
            "WHERE qr.portfolio.id = :portfolioId ")
    Page<Review> findReviewsByPortfolioId(UUID portfolioId, Pageable pageable);

    @Query("select r from Review r join fetch r.member m")
    Page<Review> findAll(Pageable pageable);

    @Query("select r from Review r " +
            "join fetch r.member m " +
            "where 1=1 and m.username like %:username% ")
    Page<Review> findAllByUsernameContains(String username, Pageable pageable);

    Page<Review> findAllByTitleContains(String title, Pageable pageable);

    Page<Review> findAllByDescriptionContains(String description, Pageable pageable);

    Page<Review> findAllByRate(double rate, Pageable pageable);

    Page<Review> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    Page<Review> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

}
