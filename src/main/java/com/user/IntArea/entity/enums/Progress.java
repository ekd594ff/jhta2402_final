package com.user.IntArea.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Progress {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    USER_CANCELLED("USER_CANCELLED"),
    SELLER_CANCELLED("SELLER_CANCELLED"),
    ADMIN_CANCELLED("ADMIN_CANCELLED");

    private final String progress;

    @Override
    public String toString() {
        return this.progress;
    }
}
