package com.user.IntArea.controller;

import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.portfolio.PortfolioUpdateDto;
import com.user.IntArea.dto.portfolio.*;
import com.user.IntArea.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // 일반

    @GetMapping("/list")
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtos(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return portfolioService.getOpenPortfolioInfoDtos(pageable);
    }

    @GetMapping("/list/company/{id}")
    public Page<PortfolioDetailInfoDto> getOpenPortfolioInfoDtosOfCompany(@PathVariable(name = "id") UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return portfolioService.getOpenPortfolioInfoDtosOfCompany(companyId, pageable);
    }

    @GetMapping("/search")
    public Page<PortfolioInfoDto> searchOpenPortfolioInfoDtos(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return portfolioService.getOpenPortfolioInfoDtosWithSearchWord(searchWord, pageable);
    }


    // (일반 권한) 검색된 포트폴리오 반환 엔드포인트
    @GetMapping("/search/detailed")
    public ResponseEntity<Page<PortfolioSearchDto>> findPortfolioBySearchWord(@RequestParam String searchWord,
                                                              @RequestParam int page,
                                                              @RequestParam int size,
                                                              @RequestParam(defaultValue = "createdAt") String sortField,
                                                              @RequestParam(defaultValue = "asc") String sortDirection) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PortfolioSearchDto> portfolioSearchDtoPage = portfolioService.findPortfolioBySearchWord(searchWord, sortField, sortDirection, pageable);
        return ResponseEntity.ok().body(portfolioSearchDtoPage);
    }

    @GetMapping("/{id}")
    public PortfolioDetailInfoDto getPortfolioInfoDto(@PathVariable(name = "id") UUID portfolioId) {
        return portfolioService.getOpenPortfolioInfoById(portfolioId);
    }

    // 본인의 portfolio가 아닌 경우 Exception을 던짐
    @GetMapping("/my/{id}")
    public PortfolioEditDetailDto getMyPortfolioInfoDto(@PathVariable(name = "id") UUID portfolioId) {
        return portfolioService.getMyPortfolioInfoById(portfolioId);
    }

//    @GetMapping("/list/random")
//    public List<PortfolioAllInfoDto> getRandomPortfolioAllInfoDtos(@RequestParam int count) {
//        return portfolioService.getRandomPortfolioAllInfoDtos(count);
//    }

    @GetMapping("/list/random")
    public List<Map<String, Object>> getRandomPortfolioInfoDtos(@RequestParam int count) {
        return portfolioService.getRandomPortfolioInfoDtos(count);
    }

    // 평점순으로 받기(평점이 같을 경우 생성일이 더 오래된 것부터 배치)
    @GetMapping("/list/recommended")
    public List<Map<String, Object>> getRecommendedPortfolioByAvgRate(@RequestParam int count) {
        Pageable pageable = PageRequest.ofSize(count);
        return portfolioService.getRecommendedPortfolioByAvgRate(pageable);
    }

    @GetMapping("/list/latest/transaction")
    public List<Map<String, Object>> getRecentTransactionPortfolioList(@RequestParam int count) {
        return portfolioService.getRecentTransactionPortfolioList(count);
    }

    // seller
    // seller
    @PostMapping
    public ResponseEntity<?> createPortfolio(PortfolioRequestDto portfolioRequestDto) {

        portfolioService.create(portfolioRequestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePortfolio(PortfolioRequestDto portfolioRequestDto) {
        portfolioService.updatePortfolio(portfolioRequestDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/list/company")
    public Page<PortfolioDetailInfoDto> getCompanyPortfolioInfoDtosByCompanyManager(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return portfolioService.getCompanyPortfolioInfoDtosByCompanyManager(pageable);
    }

    @PatchMapping("/seller/delete/{id}")
    public void deletePortfolioInfoDtoByCompany(@PathVariable(name = "id") UUID portfolioId) {
        portfolioService.softDeletePortfolio(portfolioId);
    }

    @PatchMapping("/seller/activate")
    public void updateActivatePortfolioInfoDtoByCompany(@RequestBody PortfolioIsActivatedRequestDto dto) {
        portfolioService.updateActivatePortfolio(dto);
    }

    /*@PostMapping("/draft") // 초안 임시저장 기능(서비스 매서드 미구현)
    public ResponseEntity<?> saveDraftPortfolio(@RequestBody PortfolioDraftDto portfolioDraftDto) {
        //portfolioService.saveDraft(portfolioDraftDto);
        return ResponseEntity.ok().build();
    }*/

    // admin

    @GetMapping("/admin/{id}")
    public PortfolioInfoDto getPortfolioDtoByIdByAdmin(@PathVariable UUID portfolioId) {
        return portfolioService.getPortfolioInfoByIdByAdmin(portfolioId);
    }

    @PatchMapping("/admin/activate")
    public void updateActivatePortfolioByAdmin(PortfolioIsActivatedRequestDto dto) {
        portfolioService.updateActivatePortfolio(dto);
    }

    @GetMapping("/admin/list")
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosByAdmin(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "c.companyName").descending());
        return portfolioService.getAllPortfolioInfoDtosByAdmin(pageable);
    }

    @GetMapping("/admin/list/company/{id}")
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosOfCompanyByAdmin(@PathVariable(name = "id") UUID companyId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        return portfolioService.getAllPortfolioInfoDtosOfCompanyByAdmin(companyId, pageable);
    }

    @GetMapping("/admin/search")
    public Page<PortfolioInfoDto> searchPortfolioByAdmin(@RequestParam String searchWord, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
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

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<PortfolioInfoDto>> getSearchReview(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                  @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                  @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                  @RequestParam(required = false) String filterColumn,
                                                                  @RequestParam(required = false) String filterValue) {
        if (sortField.equals("companyName")) {
            sortField = "company.companyName";
        }
        if (sortField.equals("activated")) {
            sortField = "isActivated";
        }
        if (sortField.equals("deleted")) {
            sortField = "isDeleted";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<PortfolioInfoDto> portfolioInfoDtoPage = portfolioService.getSearchPortfolio(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue), pageable);
        return ResponseEntity.ok().body(portfolioInfoDtoPage);
    }

    @DeleteMapping("/admin/{ids}")
    public ResponseEntity<?> softDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        portfolioService.softDeletePortfolios(idList);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/hard/{ids}")
    public ResponseEntity<?> hardDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        portfolioService.hardDeletePortfolios(idList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> editPortfolio(@RequestBody EditPortfolioDto editPortfolioDto) {
        portfolioService.editPortfolioForAdmin(editPortfolioDto);
        return ResponseEntity.noContent().build();
    }
}
