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

    @Column
    private String companyManager;

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

    @OneToMany(mappedBy = "id")
    private List<Portfolio> portfolios;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Company(String companyManager, Member member, String companyName, String description, String phone, String address) {
        this.companyManager = companyManager;
        this.member = member;
        this.companyName = companyName;
        this.description = description;
        this.phone = phone;
        this.address = address;
    }
}
