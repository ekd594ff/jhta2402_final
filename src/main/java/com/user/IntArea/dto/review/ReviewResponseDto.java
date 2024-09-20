package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Review;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewResponseDto {

    private UUID portfolioId;
    private UUID id;
    private String title;
    private String description;
    private double rate;

    @Builder
    public ReviewResponseDto(Review review, UUID portfolioId) {
        this.portfolioId = portfolioId;
        this.id = review.getId();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.rate = review.getRate();
    }
}
