package com.user.IntArea.controller;

import com.user.IntArea.dto.quotationRequest.*;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quotationRequest")
@RequiredArgsConstructor
public class QuotationRequestController {

    private final QuotationRequestService quotationRequestService;

    @PostMapping("/create")
    public ResponseEntity<QuotationRequestDto> createQuotationRequest(@RequestBody QuotationRequestDto requestDto) throws IllegalAccessException {
        QuotationRequestDto responseDto = quotationRequestService.createQuotationRequest(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationRequestDto> getQuotationRequest(@PathVariable UUID id) {
        QuotationRequestDto responseDto = quotationRequestService.getQuotationRequest(id);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<QuotationRequestDetailDto> getQuotationRequestDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(quotationRequestService.getQuotationRequestDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotationRequestDto> updateQuotationRequest(@PathVariable UUID id, @RequestBody QuotationRequestDto requestDto) {
        QuotationRequestDto responseDto = quotationRequestService.updateQuotationRequest(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/list")
    public ResponseEntity<Page<QuotationRequestListDto>> getQuotationRequestByMemberId(@RequestParam String progress, @RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt", "id").descending());
        Page<QuotationRequestListDto> responseDto = quotationRequestService.getQuotationRequestsByMemberId(progress, pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/companyList")
    public ResponseEntity<Page<QuotationRequestCompanyDto>> getQuotationRequestByCompanyId(@RequestParam String progress, @RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt", "id").descending());
        Page<QuotationRequestCompanyDto> responseDto = quotationRequestService.getQuotationRequestsByCompanyId(progress, pageable);
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

    @PutMapping("/seller/cancel/{id}")
    public ResponseEntity<Void> cancelSellerQuotationRequest(@PathVariable UUID id) {
        quotationRequestService.cancelSellerQuotationRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<QuotationAdminRequestDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "progress", "id").descending());
        Page<QuotationAdminRequestDto> quotationAdminRequestDtos = quotationRequestService.findAllQutationRequestDto(pageable);
        return ResponseEntity.ok().body(quotationAdminRequestDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<QuotationAdminRequestDto>> findAllByFilter(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                          @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                          @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                          @RequestParam(required = false) String filterColumn,
                                                                          @RequestParam(required = false) String filterValue) {
        if (sortField.equals("username")) {
            sortField = "m.username";
        }
        if (sortField.equals("portfolioId")) {
            sortField = "p.id";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<QuotationAdminRequestDto> quotationAdminRequestDtos = quotationRequestService.findAllByFilter(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue), pageable);
        return ResponseEntity.ok().body(quotationAdminRequestDtos);
    }

    @PatchMapping("/admin/{ids}")
    public ResponseEntity<?> deleteQuotationRequest(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        quotationRequestService.updateProgressByIds(idList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> editQuotationRequest(@RequestBody EditQuotationRequestDto editQuotationRequestDto) {
        quotationRequestService.editQuotationRequestForAdmin(editQuotationRequestDto);
        return ResponseEntity.noContent().build();
    }
}

