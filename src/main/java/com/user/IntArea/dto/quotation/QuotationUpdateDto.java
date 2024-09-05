package com.user.IntArea.dto.quotation;

import com.user.IntArea.entity.QuotationRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class QuotationUpdateDto {

    private UUID quotationRequestId;
    private Long totalTransactionAmount;
    private MultipartFile imageFiles; // 이미지는 반드시 1개 이상 첨부해야 함.
}
