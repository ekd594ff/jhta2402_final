package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private Company company;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    public Portfolio(Company company, String title, String description) {
        this.company = company;
        this.title = title;
        this.description = description;
    }
}
