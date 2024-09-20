package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Review;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewQuotationRequestDto {

    private UUID id;
    private String title;
    private String description;
    private double rate;

    public ReviewQuotationRequestDto(Review review) {
        if (review.getId() != null) {
            this.id = review.getId();
            this.title = review.getTitle();
            this.description = review.getDescription();
            this.rate = review.getRate();
        }
    }
}
