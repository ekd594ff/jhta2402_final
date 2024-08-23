package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class RequestSolution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationRequestId")
    private QuotationRequest quotationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solutionId")
    private Solution solution;

    @Builder
    public RequestSolution(QuotationRequest quotationRequest, Solution solution) {
        this.quotationRequest = quotationRequest;
        this.solution = solution;
    }
}
