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
public class Order { // 주문 요청서 - 견적이 승인되면 구매자가 판매자의 상품에 대해 구매 요청을 할 때 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private AppUser buyer;

    private LocalDateTime orderDate;
    private String status; // "PENDING", "COMPLETED"
}
