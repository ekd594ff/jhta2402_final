package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDetailInfoDto {

    private UUID id;
    private String title;
    private String description;
    private String companyName;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isActivated;

    public PortfolioDetailInfoDto(Portfolio portfolio, List<String> imageUrls) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.imageUrls = imageUrls;
        this.createdAt = portfolio.getCreatedAt();
        this.updatedAt = portfolio.getUpdatedAt();
        this.isDeleted = portfolio.isDeleted();
        this.isActivated = portfolio.isActivated();
    }
}
