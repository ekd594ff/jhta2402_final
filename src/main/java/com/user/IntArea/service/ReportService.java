package com.user.IntArea.service;

import com.user.IntArea.dto.report.ReportDto;
import com.user.IntArea.entity.Report;
import com.user.IntArea.entity.enums.ReportProgress;
import com.user.IntArea.entity.enums.ReportSort;
import com.user.IntArea.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;


    // 리뷰 신고
    public ReportDto createReviewReport(ReportDto reportDto) {
        Report report = Report.builder()
                .refId(reportDto.getRefId())
                .memberId(reportDto.getMemberId())
                .sort(ReportSort.REVIEW)
                .title(reportDto.getTitle())
                .description(reportDto.getDescription())
                .progress(ReportProgress.PENDING)
                .build();

        Report savedReport = reportRepository.save(report);

        return reportDto.builder()
                .id(savedReport.getId())
                .refId(savedReport.getRefId())
                .memberId(savedReport.getMemberId())
                .sort(savedReport.getSort())
                .title(savedReport.getTitle())
                .description(savedReport.getDescription())
                .build();
    }

    // 포트폴리오 신고
    public ReportDto createPortfolioReport(ReportDto reportDto) {
        Report report = Report.builder()
                .refId(reportDto.getRefId())
                .memberId(reportDto.getMemberId())
                .sort(ReportSort.PORTFOLIO)
                .title(reportDto.getTitle())
                .description(reportDto.getDescription())
                .progress(ReportProgress.PENDING)
                .build();

        Report savedReport = reportRepository.save(report);

        return reportDto.builder()
                .id(savedReport.getId())
                .refId(savedReport.getRefId())
                .memberId(savedReport.getMemberId())
                .sort(savedReport.getSort())
                .title(savedReport.getTitle())
                .description(savedReport.getDescription())
                .build();
    }
}
