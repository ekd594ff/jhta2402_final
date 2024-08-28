package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewPortfolioDetailDto {

    private final UUID id;
    private final String username;
    private final String title;
    private final String description;
    private final Double rate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReviewPortfolioDetailDto(Review review) {
        this.id = review.getId();
        this.username = review.getMember().getUsername();
        this.title = review.getTitle();
        this.description = review.getDescription();
        this.rate = review.getRate();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
