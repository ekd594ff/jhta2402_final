package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestInfoDto {
    private UUID memberId;
    private UUID portfolioId;
    private String title;
    private String description;
    private QuotationProgress progress;
    private List<QuotationInfoDto> quotations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuotationRequestInfoDto(QuotationRequest quotationRequest) {
        this.memberId = quotationRequest.getMember().getId();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.createdAt = getCreatedAt();
        this.updatedAt = getUpdatedAt();
    }


}
