package com.user.IntArea.controller;

import com.user.IntArea.common.jwt.TokenProvider;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.company.*;
import com.user.IntArea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final TokenProvider tokenProvider;
    private final SecurityUtil securityUtil;

    @PostMapping
    public ResponseEntity<?> create(CompanyRequestDto companyRequestDto) {

        companyService.create(companyRequestDto);

        // ROLE_SELLER 로 쿠키 업데이트
        Authentication authentication = securityUtil.updateAuthenticationRole();
        ResponseCookie accessToken = tokenProvider.getAccessToken(authentication);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                .build();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> update(CompanyRequestDto companyRequestDto) {

        companyService.update(companyRequestDto);

        return ResponseEntity.ok().build();
    }

    // Member가 보는 회사 정보
    @GetMapping("/info/{id}")
    public CompanyPortfolioDetailDto getCompanyById(@PathVariable UUID id) {

        return companyService.getCompanyById(id);
    }

    @GetMapping("/info")
    public CompanyPortfolioDetailDto getCompanyById() {

        return companyService.getCompanyInfo();
    }

    @GetMapping("/admin/unapply")
    public ResponseEntity<Page<UnAppliedCompanyDto>> getUnapply(@RequestParam int page, @RequestParam int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt", "id").descending());

        Page<UnAppliedCompanyDto> unAppliedCompanyDtoPage = companyService.getUnApply(pageable);

        return ResponseEntity.ok().body(unAppliedCompanyDtoPage);
    }


    @PatchMapping("/admin/apply/{id}")
    public ResponseEntity<?> apply(@PathVariable UUID id) {

        companyService.apply(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/list")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies(@RequestBody CompanyRequestDto companyRequestDto) {
        List<CompanyResponseDto> companies = companyService.getAllCompanies(companyRequestDto);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<CompanyResponseDto>> getAllCompany(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        Page<CompanyResponseDto> companyResponseDtoPage = companyService.getAllCompany(pageable);
        return ResponseEntity.ok().body(companyResponseDtoPage);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<CompanyResponseDto>> getSerchCompany(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                    @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                    @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                    @RequestParam(required = false) String filterColumn,
                                                                    @RequestParam(required = false) String filterValue) {

        if (sortField.equals("applied")) {
            sortField = "isApplied";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }

        Page<CompanyResponseDto> companyResponseDtoPage =
                companyService.getCompanyListByFilter(pageable, Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue));
        return ResponseEntity.ok().body(companyResponseDtoPage);
    }

    @DeleteMapping("/admin/{ids}")
    public ResponseEntity<?> softDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        companyService.softDeleteCompanies(idList);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/hard/{ids}")
    public ResponseEntity<?> hardDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        companyService.hardDeleteCompanies(idList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> editCompany(@RequestBody EditCompanyDto editCompanyDto) {
        companyService.editCompanyForAdmin(editCompanyDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list/top/{count}")
    public ResponseEntity<List<CompanyWithImageDto>> getCompaniesTopByQuotationCount(@PathVariable("count") int count) {
        List<CompanyWithImageDto> companyWithImageDtoList = companyService.findTopCompaniesByQuotationCount(count);
        return ResponseEntity.ok().body(companyWithImageDtoList);
    }
}
