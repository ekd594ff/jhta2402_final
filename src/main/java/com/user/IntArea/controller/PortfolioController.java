package com.user.IntArea.controller;

import com.user.IntArea.dto.portfolio.PortfolioCreateDto;
import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.portfolio.PortfolioUpdateDto;
import com.user.IntArea.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // 일반

    @GetMapping("/list")
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtos(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getOpenPortfolioInfoDtos(pageable);
    }

    @GetMapping("/list/company/{id}")
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtosOfCompany(@PathVariable(name = "id") UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getOpenPortfolioInfoDtosOfCompany(companyId, pageable);
    }

    @GetMapping("/search")
    public Page<PortfolioInfoDto> searchOpenPortfolioInfoDtos(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getOpenPortfolioInfoDtosWithSearchWord(searchWord, pageable);
    }

    @GetMapping("/{id}")
    public PortfolioInfoDto getPortfolioInfoDto(@PathVariable(name = "id") UUID portfolioId) {
        return portfolioService.getOpenPortfolioInfoById(portfolioId);
    }

    // seller

    @PostMapping
    public ResponseEntity<?> createPortfolio(@RequestBody PortfolioCreateDto portfolioCreateDto) {
        portfolioService.create(portfolioCreateDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> updatePortfolio(@RequestBody PortfolioUpdateDto portfolioUpdateDto) {
        portfolioService.updatePortfolio(portfolioUpdateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/company/list/company")
    public Page<PortfolioInfoDto> getCompanyPortfolioInfoDtosByCompanyManager(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getCompanyPortfolioInfoDtosByCompanyManager(pageable);
    }

    @GetMapping("/company/delete/{id}")
    public void deletePortfolioInfoDtoByCompany(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.softDeletePortfolio(portfolioId);
    }

    @GetMapping("/company/activate")
    public void activatePortfolioInfoDtoByCompany(@RequestParam UUID portfolioId, @RequestParam Boolean activated) {
        portfolioService.activatePortfolio(portfolioId, activated);
    }

    // admin

    @GetMapping("/admin/{id}")
    public PortfolioInfoDto getPortfolioDtoByIdByAdmin(@PathVariable UUID portfolioId) {
        return portfolioService.getPortfolioInfoByIdByAdmin(portfolioId);
    }

    @GetMapping("/admin/activate/{id}")
    public void activatePortfolioByAdmin(@PathVariable UUID portfolioId, @RequestParam Boolean activated) {
        portfolioService.activatePortfolio(portfolioId, activated);
    }

    @GetMapping("/admin/list")
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosByAdmin(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getAllPortfolioInfoDtosByAdmin(pageable);
    }

    @GetMapping("/admin/list/company/{id}")
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosOfCompanyByAdmin(@PathVariable(name = "id") UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getAllPortfolioInfoDtosOfCompanyByAdmin(companyId, pageable);
    }

    @GetMapping("/admin/search")
    public Page<PortfolioInfoDto> searchPortfolioByAdmin(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getAllPortfolioInfoDtosWithSearchWordByAdmin(searchWord, pageable);
    }

    @PatchMapping("/admin/update")
    public ResponseEntity<?> updatePortfolioByAdmin(@RequestBody PortfolioUpdateDto portfolioUpdateDto) {
        portfolioService.updatePortfolioByAdmin(portfolioUpdateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/delete/{id}")
    public void deletePortfolioInfoDtoByAdmin(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.softDeletePortfolioByAdmin(portfolioId);
    }

    @GetMapping("/admin/hard-delete/{id}")
    public void hardDeletePortfolioInfoDtoByAdmin(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.deletePortfolioByAdmin(portfolioId);
    }

}
