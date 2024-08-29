package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PortfolioInfoDto {

    private UUID id;
    private String title;
    private String description;
    private String companyName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
//    private List<Solution> solution;
//    private List<QuotationRequest> quotationRequests;
    private boolean isDeleted;

    public PortfolioInfoDto(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.createdDate = portfolio.getCreatedAt();
        this.updatedDate = portfolio.getUpdatedAt();
        this.isDeleted = portfolio.isDeleted();
    }

    @Builder
    public PortfolioInfoDto(UUID id, String title, String description, String companyName, LocalDateTime createdDate, LocalDateTime updatedDate, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.isDeleted = isDeleted;
    }
}
