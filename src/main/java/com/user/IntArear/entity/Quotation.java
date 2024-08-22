package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationRequestId")
    private QuotationRequest quotationRequest;

    private Long totalTransactionAmount;

    public Quotation(QuotationRequest quotationRequest, Long totalTransactionAmount) {
        this.quotationRequest = quotationRequest;
        this.totalTransactionAmount = totalTransactionAmount;
    }
}
