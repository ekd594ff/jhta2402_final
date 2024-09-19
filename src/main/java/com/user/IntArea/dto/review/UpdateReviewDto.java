package com.user.IntArea.dto.review;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateReviewDto {
    private UUID reviewId;
    private String title;
    private String description;
}
