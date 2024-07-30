package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
public class SupportTicketComment { // 고객 문의사항에 대한 코멘트 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private SupportTicket supportTicket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    private String comment;
    private LocalDateTime commentDate;

}
