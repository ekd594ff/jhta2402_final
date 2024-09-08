package com.user.IntArear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller seller;

    private String content;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "board",cascade = CascadeType.ALL)
    private List<Review> reviews;

    @ElementCollection // 여러 개의 이미지 URL을 저장하기 위한 설정
    @CollectionTable(name = "board_images", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "image_url")
    private List<String> images;
}
