package com.user.IntArear.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberResponseDto {

    private final String email;
    private final String role;
}
