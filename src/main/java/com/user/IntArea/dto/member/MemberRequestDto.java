package com.user.IntArea.dto.member;

import com.user.IntArea.entity.enums.Platform;
import lombok.Data;

@Data
public class MemberRequestDto {

    private String email;
    private String username;
    private String password;
    private Platform platform;
}
