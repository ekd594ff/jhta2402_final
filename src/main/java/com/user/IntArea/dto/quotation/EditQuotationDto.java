package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;

import java.util.UUID;

@Data
public class EditQuotationDto {
    private UUID id;
    private QuotationProgress progress;
}
