package com.user.IntArea.dto.report;


import com.user.IntArea.entity.enums.ReportProgress;
import com.user.IntArea.entity.enums.ReportSort;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReportResponseDto {

    private UUID id;
    private String refTitle;
    private String username;
    private ReportSort sort;
    private String title;
    private String description;
    private String comment;
    private ReportProgress progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReportResponseDto(Tuple tuple) {
        this.id = (UUID) tuple.get("id");
        this.refTitle = tuple.get("refTitle") != null ? (String) tuple.get("refTitle") : "제목 없음"; // Null 체크 추가
        this.username = (String) tuple.get("username");
        this.sort = (ReportSort) tuple.get("sort");
        this.title = (String) tuple.get("title");
        this.description = (String) tuple.get("description");
        this.comment = (String) tuple.get("comment");
        this.progress = (ReportProgress) tuple.get("progress");
        this.createdAt = (LocalDateTime) tuple.get("createdAt");
        this.updatedAt = (LocalDateTime) tuple.get("updatedAt");
    }
}
