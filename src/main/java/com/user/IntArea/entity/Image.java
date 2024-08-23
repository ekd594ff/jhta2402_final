package com.user.IntArea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID refId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String originalFilename;

    @Builder
    public Image(UUID refId, String url, String filename, String originalFilename) {
        this.refId = refId;
        this.url = url;
        this.filename = filename;
        this.originalFilename = originalFilename;
    }
}
