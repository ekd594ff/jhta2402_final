package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.Progress;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
public class QuotationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolioId")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @OneToMany(mappedBy = "quotationRequest")
    private List<RequestSolution> requestSolutions;

    @OneToMany(mappedBy = "quotationRequest")
    private List<Quotation> quotations;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private Progress progress;

    @Builder
    public QuotationRequest(Member member, Portfolio portfolio, String title, String description, Progress progress) {
        this.member = member;
        this.portfolio = portfolio;
        this.title = title;
        this.description = description;
        this.progress = progress != null ? progress : Progress.PENDING; // 기본값 설정
    }
}
