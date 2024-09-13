package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.Quotation;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class QuotationInfoDto {
    private UUID quotationId;
    private Long totalTransactionAmount;
    private List<String> imageUrls;


    public QuotationInfoDto(Quotation quotation, List<String> imageUrls) {
        this.quotationId = quotation.getId();
        this.totalTransactionAmount = quotation.getTotalTransactionAmount();
        this.imageUrls = imageUrls == null ? new ArrayList<>() : imageUrls;
    }
}
