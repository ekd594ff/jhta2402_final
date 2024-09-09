package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.service.ReportService;
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
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/admin/list")
    public ResponseEntity<Page<ReportResponseDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReportResponseDto> reportResponseDtos = reportService.findAllReportDto(pageable);
        return ResponseEntity.ok().body(reportResponseDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<ReportResponseDto>> getSearchReview(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                          @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                          @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                          @RequestParam(required = false) String filterColumn,
                                                                          @RequestParam(required = false) String filterValue) {
        log.info("sortField={}",sortField);
        log.info("sort={}",sort);
        log.info("filterColumn={}",filterColumn);
        log.info("filterValue={}",filterValue);

        if (sortField.equals("username")) {
            sortField = "m.username";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<ReportResponseDto> reportResponseDtos = reportService.findAllByFilter(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue),pageable);
        return ResponseEntity.ok().body(reportResponseDtos);
    }

    @DeleteMapping("/admin/hard/{ids}")
    public ResponseEntity<?> hardDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        reportService.hardDeleteReports(idList);
        return ResponseEntity.noContent().build();
    }
}
