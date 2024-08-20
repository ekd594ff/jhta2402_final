package com.user.IntArear.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExampleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "exampleMember")
    private List<Example> examples;

    @Builder
    public ExampleMember(String username) {
        this.username = username;
    }
}
