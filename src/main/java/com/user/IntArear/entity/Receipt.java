package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Receipt { // 영수증 -
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String receiptNumber;
    private LocalDateTime issueDate;
    private double amount;
    private String paymentMethod; // "CREDIT_CARD", "PAYPAL"
}
