package com.user.IntArea.dto.review;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewDto {
    private String title;
    private String description;
    private Double rate;
}