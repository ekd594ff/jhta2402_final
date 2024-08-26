package com.user.IntArea.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 신고 분류
@Getter
@AllArgsConstructor
public enum ReportSort {
    PORTFOLIO("PORTFOLIO"),
    REVIEW("REVIEW");

    private final String sort;
}

