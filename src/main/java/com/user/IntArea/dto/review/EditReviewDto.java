package com.user.IntArea.dto.review;

import lombok.Data;

import java.util.UUID;

@Data
public class EditReviewDto {
    private UUID id;
    private String title;
    private String description;
}
