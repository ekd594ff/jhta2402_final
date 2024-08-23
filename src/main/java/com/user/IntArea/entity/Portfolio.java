package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
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

    @OneToMany(mappedBy = "id")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "id")
    private List<QuotationRequest> quotationRequests;

    @Builder
    public Portfolio(Company company, String title, String description) {
        this.company = company;
        this.title = title;
        this.description = description;
    }
}
