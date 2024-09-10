package com.user.IntArea.controller;

import com.user.IntArea.dto.quotation.QuotationResponseDto;
import com.user.IntArea.dto.quotationRequest.QuotationAdminRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.service.QuotationRequestService;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotationRequest(@PathVariable UUID id) {
        quotationRequestService.deleteQuotationRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuotationRequestDto>> getAllQuotationRequests() {
        List<QuotationRequestDto> responseDtos = quotationRequestService.getAllQuotationRequests();
        return ResponseEntity.ok(responseDtos);
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
        Page<QuotationAdminRequestDto> quotationAdminRequestDtos = quotationRequestService.findAllByFilter(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue),pageable);
        return ResponseEntity.ok().body(quotationAdminRequestDtos);
    }

    @PatchMapping("/admin/{ids}")
    public ResponseEntity<?> deleteQuotationRequest(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        quotationRequestService.updateProgressByIds(idList);
        return ResponseEntity.noContent().build();
    }
}
