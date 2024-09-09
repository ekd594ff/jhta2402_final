package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.ReportProgress;
import com.user.IntArea.entity.enums.ReportSort;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID refId;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportSort sort;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column
    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportProgress progress;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Report(UUID refId, UUID memberId, ReportSort sort, String title, String description, ReportProgress progress) {
        this.refId = refId;
        this.memberId = memberId;
        this.sort = sort;
        this.title = title;
        this.description = description;
        this.progress = progress;
    }
}
