package com.user.IntArea.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    KAKAO("KAKAO"),
    SERVER("SERVER");

    private final String platform;
}