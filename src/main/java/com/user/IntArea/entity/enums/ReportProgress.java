package com.user.IntArea.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 신고 진행상황
@Getter
@AllArgsConstructor
public enum ReportProgress {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private final String progress;
}
