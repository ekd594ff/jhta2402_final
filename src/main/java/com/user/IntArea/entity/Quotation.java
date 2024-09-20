package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.QuotationProgress;
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
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quotationRequestId")
    private QuotationRequest quotationRequest;

    @Column
    private Long totalTransactionAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuotationProgress progress; // 현재 진행상태

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Builder
    public Quotation(QuotationRequest quotationRequest, Long totalTransactionAmount, QuotationProgress progress) {
        this.quotationRequest = quotationRequest;
        this.totalTransactionAmount = totalTransactionAmount;
        this.progress = progress != null ? progress : QuotationProgress.PENDING; // 기본값 설정
    }
}
