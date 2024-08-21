package com.user.IntArear.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberRequestDto {

    private String email;
    private String password;
}
