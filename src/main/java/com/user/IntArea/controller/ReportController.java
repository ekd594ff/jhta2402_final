package com.user.IntArea.controller;

import com.user.IntArea.dto.report.ReportDto;
import com.user.IntArea.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
