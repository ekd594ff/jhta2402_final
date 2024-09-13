package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.QuotationProgress;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolioId")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuotationProgress progress;

    @OneToMany(mappedBy = "quotationRequest")
    private List<Quotation> quotations;

    @OneToMany(mappedBy = "quotationRequest")
    private List<RequestSolution> requestSolutions;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public QuotationRequest(Member member, Portfolio portfolio, String title, String description, QuotationProgress progress, List<RequestSolution> requestSolutions) {
        this.member = member;
        this.portfolio = portfolio;
        this.title = title;
        this.description = description;
        this.progress = progress != null ? progress : QuotationProgress.PENDING; // 기본값 설정
    }
}
