package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
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

    @Builder
    public Solution(String title, String description, Portfolio portfolio) {
        this.title = title;
        this.description = description;
        this.portfolio = portfolio;
    }
}
