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
        this.refTitle = tuple.get("refTitle").toString() != null ? tuple.get("refTitle").toString() : "제목 없음"; // Null 체크 추가
        this.username = tuple.get("username").toString();
        this.sort = ReportSort.valueOf(tuple.get("sort").toString());
        this.title = tuple.get("title").toString();
        this.description = tuple.get("description").toString();
        this.comment = tuple.get("comment").toString();
        this.progress = ReportProgress.valueOf(tuple.get("progress").toString());
        this.createdAt = (LocalDateTime) tuple.get("createdAt");
        this.updatedAt = (LocalDateTime) tuple.get("updatedAt");
    }
}
