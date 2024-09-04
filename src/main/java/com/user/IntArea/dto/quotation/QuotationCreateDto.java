package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.QuotationRequest;
import lombok.Data;

import java.util.List;

@Data
public class QuotationCreateDto {

    private QuotationRequest quotationRequestId;
    private Long totalTransactionAmount;
    private List<String> imageUrl; // 이미지는 반드시 1개 이상 첨부해야 함.
}
