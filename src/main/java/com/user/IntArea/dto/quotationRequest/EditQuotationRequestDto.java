package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;

import java.util.UUID;

@Data
public class EditQuotationRequestDto {
    private UUID id;
    private QuotationProgress progress;
}
