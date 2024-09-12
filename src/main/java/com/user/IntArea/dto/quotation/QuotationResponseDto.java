package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.QuotationProgress;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class QuotationResponseDto {
    private UUID id;
    private Long totalTransactionAmount;
    private QuotationProgress progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuotationResponseDto(Quotation quotation) {
        this.id = quotation.getId();
        this.totalTransactionAmount = quotation.getTotalTransactionAmount();
        this.progress = quotation.getProgress();
        this.createdAt = quotation.getCreatedAt();
        this.updatedAt = quotation.getUpdatedAt();
    }
}
