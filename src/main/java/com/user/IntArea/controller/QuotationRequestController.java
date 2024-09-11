package com.user.IntArea.controller;

import com.user.IntArea.dto.quotationRequest.QuotationRequestCompanyDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestCountDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestListDto;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<QuotationRequestDto> getQuotationRequest(@PathVariable UUID id) {
        QuotationRequestDto responseDto = quotationRequestService.getQuotationRequest(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotationRequestDto> updateQuotationRequest(@PathVariable UUID id, @RequestBody QuotationRequestDto requestDto) {
        QuotationRequestDto responseDto = quotationRequestService.updateQuotationRequest(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }




    @GetMapping("/list/{memberId}")
    public ResponseEntity<Page<QuotationRequestListDto>> getQuotationRequestByMemberId(@PathVariable UUID memberId, @RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuotationRequestListDto> responseDto = quotationRequestService.getQuotationRequestsByMemberId(memberId, pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/companyList/{companyId}")
    public ResponseEntity<Page<QuotationRequestCompanyDto>> getQuotationRequestByCompanyId(@PathVariable UUID companyId, @RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuotationRequestCompanyDto> responseDto = quotationRequestService.getQuotationRequestsByCompanyId(companyId, pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/company/count")
    public ResponseEntity<QuotationRequestCountDto> getQuotationCount() {
        return ResponseEntity.ok().body(quotationRequestService.getQuotationRequestCount());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotationRequest(@PathVariable UUID id) {
        quotationRequestService.deleteQuotationRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelQuotationRequest(@PathVariable UUID id) {
        quotationRequestService.cancelQuotationRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sellerCancel/{id}")
    public ResponseEntity<Void> cancelSellerQuotationRequest(@PathVariable UUID id) {
        quotationRequestService.cancelSellerQuotationRequest(id);
        return ResponseEntity.noContent().build();
    }
}

