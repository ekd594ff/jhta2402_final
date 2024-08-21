package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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

    @OneToMany(mappedBy = "id")
    private List<ExampleComment> exampleComments;


    @Builder
    public Example(String name, String description) {
        this.name = name;
        this.description = description;
    }
}