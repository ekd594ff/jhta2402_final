package com.user.IntArea.controller;

import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotation.QuotationUpdateDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestInfoDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.service.QuotationRequestService;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
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
    @GetMapping("/list") // a● Postman Pass
    public Page<QuotationInfoDto> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getQuotationInfoDtoListTowardMember(pageable);
    }

    // 받은 quotation 을 progress에 따라 소팅하여 조회
    @GetMapping("/list/progress") // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationsByProgress(@RequestParam QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getQuotationInfoDtoListTowardMemberSortedByProgress(progress, pageable);
    }

    // seller 권한

    // (seller) quotation 생성
    @PostMapping("/company/create")
    public ResponseEntity<?> createQuotation(QuotationCreateDto quotationCreateDto) {
        quotationService.creatQuotationBySeller(quotationCreateDto);
        return ResponseEntity.ok().build();
    }

    // (seller) quotation 수정
    @PostMapping("/company/update")
    public ResponseEntity<?> modifyQuotation(@RequestBody QuotationUpdateDto quotationUpdateDto) {
        quotationService.updateQuotationBySeller(quotationUpdateDto);
        return ResponseEntity.ok().build();
    }

    // (seller) 거래종료 처리(작성한 quotation 취소 및 quotationRequest 취소)
    @PostMapping("/company/cancel")
    public void cancelQuotationAndQuotationRequest(@RequestBody QuotationRequestInfoDto quotationRequestInfoDto) {
        QuotationRequest quotationRequest = quotationRequestService.findById(quotationRequestInfoDto.getId());
        quotationService.cancelQuotationBySeller(quotationRequest);
    }

    // (seller) 작성한 전체 quotation 조회
    @GetMapping("/company/list")
    public Page<QuotationInfoDto> getAllQuotationInfoDtos(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getAllQuotationOfCompany(pageable);
    }

    // (seller) 작성한 전체 quotation 를 진행상태에 따라 조회
    @GetMapping("/company/list/{progress}")
    public Page<QuotationInfoDto> getAllQuotationInfoDtosSortedByProgress(@RequestParam QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getAllQuotationOfCompanySortedByProgress(progress, pageable);
    }

    // 작성한 전체 quotation 를 검색어로 조회
    @GetMapping("/company/list/search/{searchWord}")
    public Page<QuotationInfoDto> findQuotationInfoDtosOfCompanyBySearchword(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.findQuotationInfoDtosBySearchword(searchWord, pageable);
    }

    // admin 권한

    // (admin) 모든 quotation 를 진행상태에 따라 조회
    @GetMapping("/admin/list/{progress}")
    public Page<QuotationInfoDto> getAllQuotationInfoDtosSortedByProgressByAdmin(@RequestParam QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getAllQuotationInfoDtos(progress, pageable);
    }

    // (admin) 특정 회사가 작성한 모든 quotation 를 진행상태에 따라 조회
    @GetMapping("/admin/list/{companyId}/{progress}")
    public Page<QuotationInfoDto> getAllQuotationInfoDtosOfCompanySortedByProgressByAdmin(@RequestParam UUID companyId, @RequestParam QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return quotationService.getAllQuotationOfCompanySortedByProgressByAdmin(companyId, progress, pageable);
    }
}
