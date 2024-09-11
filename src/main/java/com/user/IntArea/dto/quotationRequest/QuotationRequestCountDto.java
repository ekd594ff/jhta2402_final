package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Data;

import java.util.List;

@Data
public class QuotationRequestCountDto {

    private Long pendingCount;
    private Long approvedCount;

    public QuotationRequestCountDto(List<Object[]> objects) {

        for (Object[] object : objects) {
            if (object[1].equals(QuotationProgress.PENDING.getProgress())) {
                pendingCount = (Long) object[2];
            } else if (object[1].equals(QuotationProgress.APPROVED.getProgress())) {
                approvedCount = (Long) object[2];
            }
        }
    }
}
