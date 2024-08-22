package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationId")
    private Quotation quotation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    public Review(Quotation quotation, Member member) {
        this.quotation = quotation;
        this.member = member;
    }
}
