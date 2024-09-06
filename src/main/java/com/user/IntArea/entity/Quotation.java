package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.Progress;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationRequestId")
    private QuotationRequest quotationRequest;

    @Column
    private Long totalTransactionAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String clientName; // 견적을 요청한 고객의 이름. 기본값은 quotationRequest에서 작성자의 이름을 따오고 없을 경우 이메일주소를 따옴

    @Column
    private String description; // 견적에 대한 간단한 설명

    @Enumerated(EnumType.STRING)
    @Column
    private Progress progress; // 현재 진행상태


    @Builder
    public Quotation(QuotationRequest quotationRequest, Long totalTransactionAmount, Progress progress) {
        this.quotationRequest = quotationRequest;
        this.totalTransactionAmount = totalTransactionAmount;
        this.clientName = quotationRequest.getMember().getUsername() != null ? quotationRequest.getMember().getUsername() : quotationRequest.getMember().getEmail();
        this.progress = progress != null ? progress : Progress.PENDING; // 기본값 설정

    }
}
