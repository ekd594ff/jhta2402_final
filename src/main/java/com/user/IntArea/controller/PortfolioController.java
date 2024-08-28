package com.user.IntArea.controller;

import com.user.IntArea.dto.portfolio.PortfolioCreateDto;
import com.user.IntArea.dto.portfolio.PortfolioDetailDto;
import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.portfolio.PortfolioUpdateDto;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

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

    @GetMapping("/company")
    public List<PortfolioInfoDto> getPortfolioDtosByCompany() {
        return portfolioService.getCompanyPortfoliosByCompanyManager();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDetailDto> getPortfolioById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(portfolioService.getPortfolioById(id));
    }


    @GetMapping("/test")
    public List<PortfolioInfoDto> test(@RequestParam String searchWord) {
        return portfolioService.test(searchWord);
    }

    @GetMapping("/search")
    public Page<PortfolioInfoDto> searchPortfolio(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return portfolioService.getPortfolioInfoDtosWithSearchWord(searchWord, pageable);
    }

    @GetMapping("/delete/{id}")
    public void deletePortfolioInfoDtoByCompany(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.softDeletePortfolio(portfolioId);
    }

    @GetMapping("/activate")
    public void activatePortfolioInfoDtoByCompany(@RequestParam UUID portfolioId, @RequestParam Boolean activated) {
        portfolioService.activatePortfolio(portfolioId, activated);
    }

    // admin

    @GetMapping("/admin/{id}")
    public PortfolioInfoDto getPortfolioDtoById(@PathVariable UUID portfolioId) {
        return portfolioService.getPortfolioInfoById(portfolioId);
    }

    @GetMapping("/test2")
    public List<PortfolioInfoDto> getAllPortfolioInfoDtos() {
        return portfolioService.getAllPortfolioInfoDtos();
    }

    @PatchMapping("/admin/update")
    public ResponseEntity<?> updatePortfolioByAdmin(@RequestBody PortfolioUpdateDto portfolioUpdateDto) {
        portfolioService.updatePortfolio(portfolioUpdateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("admin/delete/{id}")
    public void deletePortfolioInfoDtoByAdmin(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
    }

}
