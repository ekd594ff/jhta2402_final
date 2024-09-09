package com.user.IntArea.service;

import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.dto.review.ReviewPortfolioDetailDto;
import com.user.IntArea.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
