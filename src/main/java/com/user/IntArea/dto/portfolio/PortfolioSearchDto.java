package com.user.IntArea.dto.portfolio;

import lombok.*;

import java.util.UUID;

@Data
public class PortfolioSearchDto {

    private String title;
    private String companyName;
    private String description;
    private String imageUrlStr;
    private String[] imageUrls;
    private UUID portfolioId;

    public PortfolioSearchDto(String title, String companyName, String description, String[] imageUrls, String portfolioId) {
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.imageUrls = imageUrls;
        this.portfolioId = UUID.fromString(portfolioId);
    }
}
