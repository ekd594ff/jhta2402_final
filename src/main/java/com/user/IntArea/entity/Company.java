package com.user.IntArea.entity;

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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false)
    private String companyName;

    @Column
    private String description;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private Boolean isApplied;

    @OneToMany(mappedBy = "id")
    private List<Portfolio> portfolios;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Company(Member member, String companyName, String description, String phone, String address, String detailAddress, boolean isApplied) {
        this.member = member;
        this.companyName = companyName;
        this.description = description;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.isApplied = isApplied;
    }
}
