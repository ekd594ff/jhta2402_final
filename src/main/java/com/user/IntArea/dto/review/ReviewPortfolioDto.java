package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewPortfolioDto {
    private UUID id;
    private UUID portfolioId;
    private String username;
    private String title;
    private String description;
    private Double rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewPortfolioDto(Review review) {
        this.id = review.getId();
        this.portfolioId = review.getQuotation().getQuotationRequest().getPortfolio().getId();
        this.username = review.getMember().getUsername();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.rate = review.getRate();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
