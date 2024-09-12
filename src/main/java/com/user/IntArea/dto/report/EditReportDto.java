package com.user.IntArea.dto.report;

import com.user.IntArea.entity.enums.ReportProgress;
import lombok.Data;

import java.util.UUID;

@Data
public class EditReportDto {
    private UUID id;
    private String comment;
    private ReportProgress progress;
}
