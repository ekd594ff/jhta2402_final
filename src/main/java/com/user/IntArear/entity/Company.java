package com.user.IntArear.entity;

import com.user.IntArear.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    private String description;

    private String phone;

    private String address;

    public Company(Member member, String companyName, String description, String phone, String address) {
        this.member = member;
        this.companyName = companyName;
        this.description = description;
        this.phone = phone;
        this.address = address;
    }
}
