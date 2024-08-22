package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
@Table(name = "Request_solution")
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

    public RequestSolution(QuotationRequest quotationRequest, Solution solution) {
        this.quotationRequest = quotationRequest;
        this.solution = solution;
    }
}
