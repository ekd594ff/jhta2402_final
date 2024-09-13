package com.user.IntArea.entity;

import com.user.IntArea.dto.review.CreateReviewDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationId")
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private Double rate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Review(Quotation quotation, Member member) {
        this.quotation = quotation;
        this.member = member;
    }

    @Builder
    public Review(Member member, Quotation quotation, CreateReviewDto createReviewDto) {
        this.quotation = quotation;
        this.member = member;
        this.title = createReviewDto.getTitle();
        this.description = createReviewDto.getDescription();
        this.rate = createReviewDto.getRate();
    }
}
