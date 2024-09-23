package com.user.IntArea.controller;

import com.user.IntArea.dto.report.ReportDto;
import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.report.EditReportDto;
import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/create/review")
    public ResponseEntity<ReportDto> createReviewReport(@RequestBody ReportDto reportDto) {
        ReportDto createdReport = reportService.createReviewReport(reportDto);
        return ResponseEntity.ok(createdReport);
    }

    @PostMapping("/create/portfolio")
    public ResponseEntity<ReportDto> createPortfolioReport(@RequestBody ReportDto reportDto) {
        ReportDto createdReport = reportService.createPortfolioReport(reportDto);
        return ResponseEntity.ok(createdReport);
    }

    @GetMapping("/memberList/{memberId}")
    public ResponseEntity<Page<ReportDto>> getReportsByMemberId(@PathVariable UUID memberId, @RequestParam int page, @RequestParam(name= "pageSize") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReportDto> reportDtos = reportService.findReportByMemberId(memberId, pageable);
        return ResponseEntity.ok().body(reportDtos);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<ReportResponseDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        Page<ReportResponseDto> reportResponseDtos = reportService.findAllReportDto(pageable);
        return ResponseEntity.ok().body(reportResponseDtos);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<ReportResponseDto>> getSearchReview(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                   @RequestParam(defaultValue = "createdAt", required = false) String sortField,
                                                                   @RequestParam(defaultValue = "desc", required = false) String sort,
                                                                   @RequestParam(required = false) String filterColumn,
                                                                   @RequestParam(required = false) String filterValue) {
        if (sortField.equals("username")) {
            sortField = "m.username";
        }
        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<ReportResponseDto> reportResponseDtos = reportService.findAllByFilter(Optional.ofNullable(filterColumn), Optional.ofNullable(filterValue), pageable);
        return ResponseEntity.ok().body(reportResponseDtos);
    }

    @DeleteMapping("/admin/hard/{ids}")
    public ResponseEntity<?> hardDeleteMembers(@PathVariable String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        reportService.hardDeleteReports(idList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin")
    public ResponseEntity<?> editReport(@RequestBody EditReportDto editReportDto) {
        reportService.editReportForAdmin(editReportDto);
        return ResponseEntity.noContent().build();
    }
}
