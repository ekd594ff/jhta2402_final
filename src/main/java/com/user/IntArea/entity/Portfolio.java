package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
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

    @OneToMany(mappedBy = "id")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "id")
    private List<QuotationRequest> quotationRequests;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Portfolio(Company company, String title, String description) {
        this.company = company;
        this.title = title;
        this.description = description;
    }


    public List<String> getPortfolioImages() {
        List<String> images = new ArrayList<>();

        return images;
    }

    public List<Quotation> getWrittenQuotations() {
        List<Quotation> quotations = new ArrayList<>();
        return quotations;
    }
}
