package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Example { // 예제

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exampleMemberId")
    private ExampleMember exampleMember;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // name, description 주입
    // id, createdAt, updatedAt은 어노테이션에 의해 자동 생성됨
    @Builder
    public Example(String name, String description, ExampleMember exampleMember, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.exampleMember = exampleMember;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}