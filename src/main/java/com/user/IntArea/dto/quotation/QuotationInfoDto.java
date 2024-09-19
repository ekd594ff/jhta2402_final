package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class QuotationInfoDto {
    private UUID quotationId;
    private Long totalTransactionAmount;
    private List<String> imageUrls;
    private QuotationProgress progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public QuotationInfoDto(Quotation quotation, List<String> imageUrls) {
        this.quotationId = quotation.getId();
        this.totalTransactionAmount = quotation.getTotalTransactionAmount();
        this.imageUrls = imageUrls == null ? new ArrayList<>() : imageUrls;
        this.progress = quotation.getProgress();
        this.createdAt = quotation.getCreatedAt();
        this.updatedAt = quotation.getUpdatedAt();
    }
}
