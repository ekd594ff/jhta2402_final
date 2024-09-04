package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.Solution;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PortfolioAllInfoDto {

    private UUID id;
    private String title;
    private String description;
    private UUID companyId;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private List<String> imageUrl;
    private List<Solution> solutions;
    private List<QuotationRequest> quotationRequests;
    private List<Quotation> quotations;


    public PortfolioAllInfoDto(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyId = portfolio.getCompany().getId();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.createdAt = portfolio.getCreatedAt();
        this.updatedAt = portfolio.getUpdatedAt();
        this.isDeleted = portfolio.isDeleted();
        this.imageUrl = portfolio.getPortfolioImages();
        this.solutions = portfolio.getSolutions();
        this.quotationRequests = portfolio.getQuotationRequests();
        this.quotations = portfolio.getWrittenQuotations();
    }

    @Builder
    public PortfolioAllInfoDto(UUID id, String title, String description, UUID companyId, String companyName, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted, List<String> imageUrl, List<Solution> solutions, List<QuotationRequest> quotationRequests, List<Quotation> quotations) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.companyId = companyId;
        this.companyName = companyName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.imageUrl = imageUrl;
        this.solutions = solutions;
        this.quotationRequests = quotationRequests;
        this.quotations = quotations;
    }
}
