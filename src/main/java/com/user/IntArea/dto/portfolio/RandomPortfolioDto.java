package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;

import java.time.LocalDateTime;
import java.util.UUID;

public class RandomPortfolioDto extends PortfolioInfoDto {
    private String thumbnail;

    public RandomPortfolioDto(UUID id, String title, String description, String companyName, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted, String url) {
        super(id, title, description, companyName, createdAt, updatedAt, isDeleted);
        this.thumbnail = url;
    }
}
