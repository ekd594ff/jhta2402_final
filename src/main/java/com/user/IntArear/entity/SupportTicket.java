package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SupportTicket { // 고객의 문의사항
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    private String issue;
    private String status; //  "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"
    private LocalDateTime createdDate;
    private LocalDateTime resolvedDate;
    private String priority; //  "LOW", "MEDIUM", "HIGH"

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private AppUser assignedTo; // 지원 요청을 처리 중인 지원 팀원 (optional)

    @OneToMany(mappedBy = "supportTicket", cascade = CascadeType.ALL)
    private List<SupportTicketComment> comments = new ArrayList<>();
}
