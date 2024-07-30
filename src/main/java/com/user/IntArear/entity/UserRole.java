package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
public class UserRole { // 사용자와 역할 간의 다대다 관계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
