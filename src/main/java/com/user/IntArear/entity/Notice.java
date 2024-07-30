package com.user.IntArear.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notice { // 관리자가 운영하는 공지사항
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AppUser author; // 공지사항을 작성한 관리자

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeComment> comments = new ArrayList<>();
}
