package com.user.IntArea.controller;

import com.user.IntArea.dto.quotation.EditQuotationDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotation.QuotationUpdateDto;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.service.QuotationRequestService;
import com.user.IntArea.dto.quotation.QuotationResponseDto;
import com.user.IntArea.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;
    private final QuotationRequestService quotationRequestService;


    // 일반 권한

    // (일반) 받은 특정한 quotation 및 quotationRequest 승인처리 // ● Postman Pass
    @PatchMapping("/approve/{quotationId}")
    public ResponseEntity<?> approveQuotation(@PathVariable(name = "quotationId") UUID quotationId) {
        quotationService.approveQuotationByCustomer(quotationId);
        return ResponseEntity.ok().build();
    }

    // (일반) 받은 특정한 quotation 거부 (고객이 quotation만 취소) // ● Postman Pass
    @PatchMapping("/cancel/{id}")
    public void cancelQuotation(@PathVariable(name = "id") UUID quotationId) {
        Quotation quotation = quotationService.getById(quotationId);
        quotationService.cancelQuotationByCustomer(quotation);
    }

    // (일반) (받은 견적서가 있을 때) 고객에 의한 완전한 거래 취소 (quotation 및 quotationRequest 취소) // ● Postman Pass
    @PatchMapping("/cancel/contract/{quotationId}") // ● Postman Pass
    public void cancelQuotationAndQuotationRequestByCustomer(@PathVariable(name = "quotationId") UUID quotationId) {
        quotationService.cancelContractByCustomer(quotationId);
    }

    // (일반) 받은 모든 quotation 조회
    @GetMapping("/list") // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getQuotationInfoDtoListTowardMember(pageable);
    }

    // (일반) 받은 특정한 quotation 정보 불러오기
    @GetMapping("/{quotationId}") // ● Postman Pass
    public QuotationInfoDto getQuotationByMember(@PathVariable UUID quotationId) {
        return quotationService.getQuotationInfoDtoByMember(quotationId);
    }

    // (일반) 받은 모든 quotation 을 progress에 따라 소팅하여 조회
    @GetMapping("/list/sorted/{progress}")
    public Page<QuotationInfoDto> getAllQuotationsByProgress(@PathVariable(name = "progress") QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getQuotationInfoDtoListTowardMemberSortedByProgress(progress, pageable);
    }


    // seller 권한

    // (seller) quotation 생성
    @PostMapping("/company/create") // ● Postman Pass
    public ResponseEntity<?> createQuotation(QuotationCreateDto quotationCreateDto) {
        quotationService.createQuotationBySeller(quotationCreateDto);
        return ResponseEntity.ok().build();
    }

    // (seller) quotation 수정(신규 생성 및 기존 quotation 취소처리)
    @PostMapping("/company/update")// ● Postman Pass
    public ResponseEntity<?> updateQuotation(QuotationUpdateDto quotationUpdateDto) {
        quotationService.updateQuotationBySeller(quotationUpdateDto);
        return ResponseEntity.ok().build();
    }

    // (seller) 작성한 quotation 취소 처리
    @PatchMapping("/company/cancel/{id}") // ● Postman Pass
    public void cancelQuotationBySeller(@PathVariable UUID id) {
        quotationService.cancelQuotationBySeller(id);
    }

    // (seller) 작성한 특정한 quotation 정보 불러오기
    @GetMapping("/company/{quotationId}") // ● Postman Pass
    public QuotationInfoDto getQuotationByCompany(@PathVariable UUID quotationId) {
        return quotationService.getQuotationInfoDtoByCompany(quotationId);
    }

    // admin 권한

    // (admin) 특정한 quotation 정보 불러오기
    @GetMapping("/admin/{quotationId}") // ● Postman Pass
    public QuotationInfoDto getQuotationByAdmin(@PathVariable UUID quotationId) {
        return quotationService.getQuotationInfoDtoByAdmin(quotationId);
    }


    @GetMapping("/admin/list")
    public ResponseEntity<Page<QuotationResponseDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        Page<QuotationResponseDto> quotationResponseDtos = quotationService.findAllQutationResponseDto(pageable);
        return ResponseEntity.ok().body(quotationResponseDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<QuotationResponseDto>> getSearchMember(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                      @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                      @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                      @RequestParam(required = false) String filterColumn,
                                                                      @RequestParam(required = false) String filterValue) {
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
        quotationService.updateProgress(idList);
        return ResponseEntity.noContent().build();
    }

    // quotation 수정

    // (seller) 거래종료 처리(작성한 quotation 취소 및 quotationRequest 취소)
    @GetMapping("/company/cancel/contract") // ● Postman Pass
    public void cancelQuotationAndQuotationRequest(@RequestParam UUID quotationId) {
        quotationService.cancelQuotationAndQuotationRequestBySeller(quotationId);
    }

    // (seller) 작성한 전체 quotation 조회
    @GetMapping("/company/list")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtos(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsOfCompany(pageable);
    }

    // (seller) 작성한 전체 quotation 를 진행상태에 따라 조회
    @GetMapping("/company/list/sorted/{progress}")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtosSortedByProgress(@PathVariable(name = "progress") QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsOfCompanySortedByProgress(progress, pageable);
    }

    // (seller) 작성한 전체 quotation 를 검색어로 조회
    @GetMapping("/company/list/search")  // ● Postman Pass
    public Page<QuotationInfoDto> getQuotationInfoDtosOfCompanyBySearchword(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsOfCompanyBySearchword(searchWord, pageable);
    }

    // admin 권한

    // (admin) 모든 quotation 를 조회
    @GetMapping("/admin/list/all")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtosByAdmin(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsByAdmin(pageable);
    }

    // (admin) 모든 quotation 를 진행상태에 따라 조회
    @GetMapping("/admin/list/all/{progress}")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtosSortedByProgressByAdmin(@PathVariable(name = "progress") QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsByProgressByAdmin(progress, pageable);
    }

    // (admin) 특정 회사가 작성한 모든 quotation 를 조회
    @GetMapping("/admin/list/{companyId}")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtosOfCompanyByAdmin(@PathVariable(name = "companyId") UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsOfCompanyByAdmin(companyId, pageable);
    }

    // (admin) 특정 회사가 작성한 모든 quotation 를 진행상태에 따라 조회
    @GetMapping("/admin/list/{companyId}/{progress}")  // ● Postman Pass
    public Page<QuotationInfoDto> getAllQuotationInfoDtosOfCompanySortedByProgressByAdmin(@PathVariable(name = "companyId") UUID companyId, @PathVariable(name = "progress") QuotationProgress progress, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return quotationService.getAllQuotationsOfCompanySortedByProgressByAdmin(companyId, progress, pageable);
    }

    // (admin) 특정 견적서 진행상태 수정
    @PatchMapping("/admin")
    public ResponseEntity<?> editQuotation(@RequestBody EditQuotationDto editQuotationDto) {
        quotationService.editQuotationForAdmin(editQuotationDto);
        return ResponseEntity.noContent().build();
    }
}
