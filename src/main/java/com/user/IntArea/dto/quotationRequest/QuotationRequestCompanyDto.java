package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.member.QuotationRequestMemberDto;
import com.user.IntArea.dto.portfolio.PortfolioQuotationRequestDto;
import com.user.IntArea.dto.review.ReviewQuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestCompanyDto {
    private UUID id;
    private QuotationRequestMemberDto member;
    private PortfolioQuotationRequestDto portfolio;
    private String title;
    private String description;
    private String progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SolutionDto> solutions;
    private ReviewQuotationRequestDto review;
    private UUID companyId;

    @Builder
    public QuotationRequestCompanyDto(
            QuotationRequest quotationRequest,
            String memberUrl,
            String portfolioUrl,
            List<SolutionDto> solutions,
            UUID companyId,
            Review review
    ) {
        this.id = quotationRequest.getId();
        this.member = new QuotationRequestMemberDto(quotationRequest.getMember(), memberUrl);
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress().toString();
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
        this.portfolio = new PortfolioQuotationRequestDto(quotationRequest.getPortfolio(), portfolioUrl);
        this.solutions = solutions;
        this.review = new ReviewQuotationRequestDto(review);
        this.companyId = companyId;
    }
}
