package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
public class Quotation { // 견적서 - 고객이 상품에 대해 문의하거나 가격을 요청할 때 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private AppUser buyer;

    private double quotedPrice;
    private LocalDateTime quotationDate;
    private String status; // "REQUESTED", "APPROVED", "REJECTED"
}
