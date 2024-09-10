package com.user.IntArea.dto.quotation;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class QuotationCreateDto {

    private UUID quotationRequestId;
    private Long totalTransactionAmount;
    private List<MultipartFile> imageFiles; // 이미지는 반드시 1개 이상 첨부해야 함.
}
