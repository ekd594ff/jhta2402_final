package com.user.IntArea.entity;

import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "member")
    private Company Company;

    @OneToMany(mappedBy = "id")
    private List<QuotationRequest> quotationRequests;

    @OneToMany(mappedBy = "id")
    private List<Review> reviews;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String username, String password, String email, Platform platform, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.platform = platform;
        this.role = role;
    }

}