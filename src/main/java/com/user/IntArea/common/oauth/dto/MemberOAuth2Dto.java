package com.user.IntArea.common.oauth.dto;

import com.user.IntArea.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberOAuth2Dto {

    private String email;
    private String username;
    private Role role;
}
