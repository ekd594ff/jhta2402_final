package com.user.IntArear.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Cart { // 사용자 장바구니 - 사용자마다 1개의 장바구니를 가짐
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @OneToMany
    private List<CartItem> items;
}
