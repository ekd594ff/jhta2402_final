package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.requestSolution.RequestSolutionDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private List<RequestSolutionDto> requestSolutionDtos;
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

    // RequestSolution 및 Quotation 포함한 생성자
    public QuotationRequestInfoDto(QuotationRequest quotationRequest, List<QuotationInfoDto> quotationInfoDtos, List<RequestSolutionDto> requestSolutionDtos) {
        this.id = quotationRequest.getId();
        this.memberId = quotationRequest.getMember().getId();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.quotationInfoDtos = quotationInfoDtos;
        this.requestSolutionDtos = requestSolutionDtos;
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
    }

    // RequestSolution 및 Quotation 정보를 엔티티에서 DTO로 변환하는 로직
    public static QuotationRequestInfoDto fromEntity(QuotationRequest quotationRequest) {
        List<QuotationInfoDto> quotationInfoDtos = quotationRequest.getQuotations().stream()
                .map(QuotationInfoDto::new)
                .collect(Collectors.toList());

        List<RequestSolutionDto> requestSolutionDtos = quotationRequest.getRequestSolutions().stream()
                .map(RequestSolutionDto::new)
                .collect(Collectors.toList());

        return new QuotationRequestInfoDto(quotationRequest, quotationInfoDtos, requestSolutionDtos);
    }
}
