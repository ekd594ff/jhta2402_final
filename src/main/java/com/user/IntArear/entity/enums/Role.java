package com.user.IntArear.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CORP("ROLE_CORP"),
    ROLE_USER("ROLE_USER");

    private final String role;
}
