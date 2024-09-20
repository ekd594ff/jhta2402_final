package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.portfolio.PortfolioQuotationRequestDto;
import com.user.IntArea.dto.review.ReviewQuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Portfolio;
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
public class QuotationRequestListDto {
    private UUID id;
    private UUID memberId;
    private PortfolioQuotationRequestDto portfolio;
    private String title;
    private String description;
    private List<SolutionDto> solutions;
    private ReviewQuotationRequestDto review;
    private String progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuotationRequestListDto(
            QuotationRequest quotationRequest,
            UUID memberId,
            Portfolio portfolio,
            String portfolioUrl,
            List<SolutionDto> solutions,
            Review review) {
        this.id = quotationRequest.getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress().toString();
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
        this.memberId = memberId;
        this.portfolio = new PortfolioQuotationRequestDto(portfolio, portfolioUrl);
        this.solutions = solutions;
        this.review = new ReviewQuotationRequestDto(review);
    }
}
