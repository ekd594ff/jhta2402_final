package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewInfoListDto {

    private final UUID id;
    private final String title;
    private final String description;
    private final Double rate;
    private final String author;
    private final UUID quotationRequestId;
    private final UUID quotationId;
    private final Long totalTransactionAmount;
    private final UUID portfolioId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReviewInfoListDto(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.rate = review.getRate();
        this.author = review.getMember().getUsername();
        this.quotationRequestId = review.getQuotation().getQuotationRequest().getId();
        this.quotationId = review.getQuotation().getId();
        this.totalTransactionAmount = review.getQuotation().getTotalTransactionAmount();
        this.portfolioId = review.getQuotation().getQuotationRequest().getPortfolio().getId();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}