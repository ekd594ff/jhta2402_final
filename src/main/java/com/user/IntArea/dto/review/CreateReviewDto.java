package com.user.IntArea.dto.review;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewDto {
    private Double rate;
    private String title;
    private String description;
}