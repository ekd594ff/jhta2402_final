package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.dto.quotation.QuotationResponseDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
@Slf4j
public class QuotationController {

    private final QuotationService quotationService;

    // seller 권한

    @PostMapping
    public ResponseEntity<?> createQuotation(@RequestBody QuotationCreateDto quotationCreateDto) {
        quotationService.create(quotationCreateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<QuotationResponseDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<QuotationResponseDto> quotationResponseDtos = quotationService.findAllQutationResponseDto(pageable);
        return ResponseEntity.ok().body(quotationResponseDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<QuotationResponseDto>> getSearchMember(@RequestParam int page, @RequestParam(name = "pageSize") int size,
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

        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<QuotationResponseDto> quotationResponseDtos = quotationService.findAllByFilter(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue), pageable);
        return ResponseEntity.ok().body(quotationResponseDtos);
    }

    @PatchMapping("/admin/progress/{ids}")
    public ResponseEntity<?> softDeleteQuotations(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        quotationService.updateProgess(idList);
        return ResponseEntity.noContent().build();
    }

}
