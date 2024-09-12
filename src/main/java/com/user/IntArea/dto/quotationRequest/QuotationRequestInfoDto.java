package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.requestSolution.RequestSolutionDto;
import com.user.IntArea.dto.solution.SolutionForQuotationRequestDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestInfoDto {
    private UUID id;
    private UUID memberId;
    private UUID portfolioId;
    private String title;
    private String description;
    private QuotationProgress progress;
    private List<QuotationInfoDto> quotationInfoDtos;
    private List<SolutionForQuotationRequestDto> solutionsForQuotationRequest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자: 외래키 관계를 제외한 데이터만 초기화
    public QuotationRequestInfoDto(QuotationRequest quotationRequest) {
        this.id = quotationRequest.getId();
        this.memberId = quotationRequest.getMember().getId();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
    }

    // solutions 및 Quotations 를 포함한 생성자
    public QuotationRequestInfoDto(QuotationRequest quotationRequest, List<QuotationInfoDto> quotationInfoDtos, List<SolutionForQuotationRequestDto> solutionsForQuotationRequest) {
        this.id = quotationRequest.getId();
        this.memberId = quotationRequest.getMember().getId();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.quotationInfoDtos = quotationInfoDtos;
        this.solutionsForQuotationRequest = solutionsForQuotationRequest;
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
    }

}
