package com.user.IntArea.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 견적신청서, 견적서 진행상태
@Getter
@AllArgsConstructor
public enum QuotationProgress {
    PENDING("PENDING"),
    USER_CANCELLED("USER_CANCELLED"),
    SELLER_CANCELLED("SELLER_CANCELLED"),
    ADMIN_CANCELLED("ADMIN_CANCELLED"),
    APPROVED("APPROVED");

    private final String progress;
}
