package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sort;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String progress;

    @Builder
    public Report(String sort, String title, String description, String progress) {
        this.sort = sort;
        this.title = title;
        this.description = description;
        this.progress = progress;
    }
}
