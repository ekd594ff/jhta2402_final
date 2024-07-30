package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Address { // 시공 또는 상품 배송을 위한 사용자 주소
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    private String streetAddress1;
    private String streetAddress2;
    private String zipCode;
    private String country;
}
