package com.user.IntArear.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessType {
    SOLE_PROPRIETOR("SOLE_PROPRIETOR"),  // 개인사업자
    CORPORATION("CORPORATION"),      // 법인사업자
    BRAND_AGENCY("BRAND_AGENCY");      // 브랜드대리점

    private final String BusinessType;
}
