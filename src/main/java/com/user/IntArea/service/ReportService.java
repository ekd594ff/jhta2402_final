package com.user.IntArea.service;

import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
