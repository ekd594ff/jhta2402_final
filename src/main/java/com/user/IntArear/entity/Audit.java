package com.user.IntArear.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
public class Audit { // 데이터 변경사항 추적
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;
    private Long entityId;
    private String action; // e.g., "CREATE", "UPDATE", "DELETE"
    private LocalDateTime actionDate;
    private String performedBy;
}
