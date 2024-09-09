package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QuotationAdminRequestDto {
    private UUID id;
    private String username;
    private UUID portfolioId;
    private String title;
    private String description;
    private QuotationProgress progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuotationAdminRequestDto(QuotationRequest quotationRequest) {
        this.id = quotationRequest.getId();
        this.username = quotationRequest.getMember().getUsername();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.createdAt = quotationRequest.getCreatedAt();
        this.updatedAt = quotationRequest.getUpdatedAt();
    }
}
