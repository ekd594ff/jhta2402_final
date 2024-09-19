package com.user.IntArea.service;

import com.user.IntArea.dto.report.EditReportDto;
import com.user.IntArea.dto.report.ReportDto;
import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.entity.Report;
import com.user.IntArea.entity.enums.ReportProgress;
import com.user.IntArea.entity.enums.ReportSort;
import com.user.IntArea.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Page<ReportResponseDto> findAllReportDto(Pageable pageable) {
        return reportRepository.findAllReportDto(pageable).map(ReportResponseDto::new);
    }

    public void hardDeleteReports(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        reportRepository.deleteAllById(ids);
    }

    public Page<ReportResponseDto> findAllByFilter(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "refTitle" -> {
                    return reportRepository.findAllByRefTitleContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "title" -> {
                    return reportRepository.findAllByTitleContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "username" -> {
                    return reportRepository.findAllByUsernameContains(filterValue.get(), pageable).map(ReportResponseDto ::new);
                }
                case "sort" -> {
                    return reportRepository.findAllBySortContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "description" -> {
                    return reportRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "comment" -> {
                    return reportRepository.findAllByCommentContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "progress" -> {
                    return reportRepository.findAllByProgressContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "createdAt" -> {
                    return reportRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
                case "updatedAt" -> {
                    return reportRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(ReportResponseDto::new);
                }
            }
        } else {
            return reportRepository.findAllReportDto(pageable).map(ReportResponseDto::new);
        }
        throw new RuntimeException("findAllByFilter");
    }

    @Transactional
    public void editReportForAdmin(EditReportDto editReportDto) {
        Optional<Report> reportOptional = reportRepository.findById(editReportDto.getId());
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            report.setComment(editReportDto.getComment());
            report.setProgress(editReportDto.getProgress());
        } else {
            throw new NoSuchElementException("editReportForAdmin");
        }
    }


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

    // (유저가 작성한) 신고 리스트
    public Page<ReportDto> findReportByMemberId(UUID memberId, Pageable pageable) {
        return reportRepository.findAllByMemberId(memberId, pageable);
    }
}
