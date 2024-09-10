package com.user.IntArea.entity;

import com.user.IntArea.dto.solution.SolutionDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
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

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "solution")
    private List<RequestSolution> requestSolutions;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Solution(String title, String description, Portfolio portfolio, int price) {
        this.title = title;
        this.description = description;
        this.portfolio = portfolio;
        this.price = price;
    }

    public SolutionDto toSolutionDto() {
        return SolutionDto.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .price(this.price)
                .createdAt(this.createdAt)
                .build();
    }
}
