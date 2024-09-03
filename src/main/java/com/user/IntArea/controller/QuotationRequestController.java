package com.user.IntArea.controller;

import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotationRequest")
@RequiredArgsConstructor
public class QuotationRequestController {

    private final QuotationRequestService quotationRequestService;

    @PostMapping("/create")
    public ResponseEntity<QuotationRequestDto> createQuotationRequest(@RequestBody QuotationRequestDto quotationRequestDto) {
        QuotationRequestDto createdRequest = quotationRequestService.createQuotationRequest(quotationRequestDto);
        return ResponseEntity.ok(createdRequest);
    }

}
