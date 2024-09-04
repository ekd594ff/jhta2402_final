package com.user.IntArea.controller;

import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    // seller 권한

    @GetMapping
    public ResponseEntity<?> createQuotation(@RequestBody QuotationCreateDto quotationCreateDto) {
        return quotationService.create();
    }


}
