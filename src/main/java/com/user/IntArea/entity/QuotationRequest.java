package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class QuotationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolioId")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @OneToMany(mappedBy = "id")
    private List<RequestSolution> requestSolutions;

    @Builder
    public QuotationRequest(Member member, Portfolio portfolio, String title, String content) {
        this.member = member;
        this.portfolio = portfolio;
        this.title = title;
        this.content = content;
    }
}
