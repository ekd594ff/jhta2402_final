package com.user.IntArea.controller;

import com.user.IntArea.dto.company.CompanyPortfolioDetailDto;
import com.user.IntArea.dto.company.CompanyRequestDto;
import com.user.IntArea.dto.company.CompanyResponseDto;
import com.user.IntArea.dto.company.UnAppliedCompanyDto;
import com.user.IntArea.entity.Company;
import com.user.IntArea.service.CompanyService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> create(CompanyRequestDto companyRequestDto) {

        companyService.create(companyRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> update(CompanyRequestDto companyRequestDto) {

        companyService.update(companyRequestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public CompanyPortfolioDetailDto getCompanyById() {

        return companyService.getCompanyById();
    }

    @GetMapping("/admin/unapply")
    public ResponseEntity<Page<UnAppliedCompanyDto>> getUnapply(@RequestParam int page, @RequestParam int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending());

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
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CompanyResponseDto> companyResponseDtoPage = companyService.getAllCompany(pageable);
        return ResponseEntity.ok().body(companyResponseDtoPage);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<CompanyResponseDto>> getSerchCompany(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                    @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                    @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                    @RequestParam(required = false) String filterColumn,
                                                                    @RequestParam(required = false) String filterValue) {
        log.info("sortField={}",sortField);
        log.info("sort={}",sort);
        log.info("filterColumn={}",filterColumn);
        log.info("filterValue={}", filterValue);


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

    @DeleteMapping("/admin/soft/{ids}")
    public ResponseEntity<?> softDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        companyService.softDeleteCompanys(idList);
        return ResponseEntity.noContent().build();
    }

}
