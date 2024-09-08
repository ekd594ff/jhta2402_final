package com.user.IntArea.dto.report;


import com.user.IntArea.entity.enums.ReportProgress;
import com.user.IntArea.entity.enums.ReportSort;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReportResponseDto {

    private UUID id;
    private String refName;
    private String memberName;
    private ReportSort sort;
    private String title;
    private String description;
    private ReportProgress progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
