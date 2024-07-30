package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
public class Transaction { // 포인트 사용 등 금융거래를 위한 트랜잭션
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    private double amount;
    private LocalDateTime transactionDate;
    private String transactionType; // "DEPOSIT", "WITHDRAWAL"
}
