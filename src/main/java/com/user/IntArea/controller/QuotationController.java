package com.user.IntArea.controller;

import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    // seller 권한

    @PostMapping
    public ResponseEntity<?> createQuotation(@RequestBody QuotationCreateDto quotationCreateDto) {
        quotationService.create(quotationCreateDto);
        return ResponseEntity.ok().build();
    }

}
