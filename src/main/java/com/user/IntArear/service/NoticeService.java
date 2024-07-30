package com.user.IntArear.service;

import com.user.IntArear.entity.Notice;
import com.user.IntArear.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice createNotice(Notice notice) {
        notice.setCreatedDate(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    public Notice updateNotice(Long id, Notice notice) {
        Notice existingNotice = noticeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Notice not found"));
        existingNotice.setTitle(notice.getTitle());
        existingNotice.setContent(notice.getContent());
        existingNotice.setModifiedDate(LocalDateTime.now());
        return noticeRepository.save(existingNotice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}
