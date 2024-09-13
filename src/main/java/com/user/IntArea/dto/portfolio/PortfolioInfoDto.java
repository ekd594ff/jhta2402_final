package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PortfolioInfoDto {

    private UUID id;
    private String title;
    private String description;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isActivated;

    public PortfolioInfoDto(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.createdAt = portfolio.getCreatedAt();
        this.updatedAt = portfolio.getUpdatedAt();
        this.isDeleted = portfolio.isDeleted();
        this.isActivated = portfolio.isActivated();
    }

    @Builder
    public PortfolioInfoDto(UUID id, String title, String description, String companyName, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }
}
