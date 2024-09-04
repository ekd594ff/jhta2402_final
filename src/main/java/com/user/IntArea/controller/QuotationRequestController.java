package com.user.IntArea.controller;

import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quotationRequest")
@RequiredArgsConstructor
public class QuotationRequestController {

    private final QuotationRequestService quotationRequestService;

    @PostMapping("/create")
    public ResponseEntity<QuotationRequestDto> createQuotationRequest(@RequestBody QuotationRequestDto requestDto) {
        QuotationRequestDto responseDto = quotationRequestService.createQuotationRequest(requestDto);
        return ResponseEntity.ok().build();
    }

}
