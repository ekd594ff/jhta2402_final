package com.user.IntArea.dto.portfolio;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioSearchDto {

    private String title;
    private String companyName;
    private String description;
    private String imageUrlStr;
    private String[] imageUrls;

    public PortfolioSearchDto(String title, String companyName, String description, String[] imageUrls) {
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.imageUrls = imageUrls;
    }
}
