package com.user.IntArear.entity;

import com.user.IntArear.entity.Portfolio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolioId")
    private Portfolio portfolio;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    public Solution(Portfolio portfolio, String title, String description) {
        this.portfolio = portfolio;
        this.title = title;
        this.description = description;
    }
}
