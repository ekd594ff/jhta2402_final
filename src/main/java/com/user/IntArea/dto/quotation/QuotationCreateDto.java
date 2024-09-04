package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.QuotationRequest;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class QuotationCreateDto {

    private QuotationRequest quotationRequestId;
    private Long totalTransactionAmount;
    private List<String> imageUrl; // 이미지는 반드시 1개 이상 첨부해야 함.
}
