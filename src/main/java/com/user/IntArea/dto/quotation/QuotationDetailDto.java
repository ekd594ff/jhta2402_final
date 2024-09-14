package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QuotationDetailDto {

    private UUID id;
    private Long totalTransactionAmount;
    private QuotationProgress progress;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuotationDetailDto(Quotation quotation, List<String> imageUrls) {
        this.id = quotation.getId();
        this.totalTransactionAmount = quotation.getTotalTransactionAmount();
        this.progress = quotation.getProgress();
        this.imageUrls = imageUrls;
        this.createdAt = quotation.getCreatedAt();
        this.updatedAt = quotation.getUpdatedAt();
    }
}
