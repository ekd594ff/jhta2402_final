package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestInfoDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.service.QuotationRequestService;
import com.user.IntArea.dto.quotation.QuotationResponseDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
@Slf4j
public class QuotationController {

    private final QuotationService quotationService;
    private final QuotationRequestService quotationRequestService;


    // 일반 권한

    // 받은 특정한 quotation 거부(고객이 quotation만 취소)
    @PostMapping("/cancel/{id}")
    public void cancelQuotation(@RequestParam UUID id) {
        QuotationRequest quotationRequest = quotationRequestService.findById(id);
        quotationService.cancelQuotationByCustomer(quotationRequest);
    }

    // 받은 모든 quotation 조회
    @GetMapping("/list")
    public Page<QuotationInfoDto> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getQuotationInfoDtoListTowardMember(pageable);
    }

    // 받은 quotation 을 progress에 따라 소팅하여 조회

    // 받은 quotation 을 시간순에 따라 조회

    // 검색 기능 - 받은 quotation 에 대해 totalTransactionAmount 값으로 검색



    // seller 권한

    // quotation 생성
    @PostMapping("/company/create")
    public ResponseEntity<?> createQuotation(@RequestBody QuotationCreateDto quotationCreateDto) {
        quotationService.creatQuotationBySeller(quotationCreateDto);
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

    // quotation 수정

    // 거래종료 처리(작성한 quotation 취소 및 quotationRequest 취소)


    // 작성한 전체 quotation 조회

    // 작성한 전체 quotation 를 진행상태에 따라 조회

    // 작성한 전체 quotation 를 검색어로 조회



    //

}
