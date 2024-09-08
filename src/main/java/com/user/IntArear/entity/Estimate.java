package com.user.IntArear.entity;

import com.user.IntArear.entity.enums.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

}
