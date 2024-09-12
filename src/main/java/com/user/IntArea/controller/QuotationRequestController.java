package com.user.IntArea.controller;

import com.user.IntArea.dto.quotationRequest.QuotationRequestCompanyDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestCountDto;
import com.user.IntArea.dto.quotation.QuotationResponseDto;
import com.user.IntArea.dto.quotationRequest.EditQuotationRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationAdminRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestListDto;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quotationRequest")
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/admin/list")
    public ResponseEntity<Page<QuotationAdminRequestDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<QuotationAdminRequestDto> quotationAdminRequestDtos = quotationRequestService.findAllQutationRequestDto(pageable);
        return ResponseEntity.ok().body(quotationAdminRequestDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<QuotationAdminRequestDto>> findAllByFilter(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                          @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                          @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                          @RequestParam(required = false) String filterColumn,
                                                                          @RequestParam(required = false) String filterValue) {
        log.info("sortField={}", sortField);
        log.info("sort={}", sort);
        log.info("filterColumn={}", filterColumn);
        log.info("filterValue={}", filterValue);
        System.out.println(filterColumn);
        System.out.println(filterValue);
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

