package com.user.IntArea.dto.portfolio;

import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Portfolio;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<SolutionDto> solutions;
    private List<QuotationRequestDto> quotationRequests;
    private List<QuotationInfoDto> quotations;
    private List<String> imageUrls ;

    @Builder
    public PortfolioAllInfoDto(Portfolio portfolio, List<SolutionDto> solutions, List<QuotationRequestDto> quotationRequests, List<QuotationInfoDto> quotations, List<String> imageUrls) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyId = portfolio.getCompany().getId();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.createdAt = portfolio.getCreatedAt();
        this.updatedAt = portfolio.getUpdatedAt();
        this.isDeleted = portfolio.isDeleted();
        this.solutions = solutions;
        this.quotationRequests = quotationRequests;
        this.quotations = quotations;
        this.imageUrls = imageUrls == null ? new ArrayList<>() : imageUrls;
    }
}
