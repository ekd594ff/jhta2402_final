package com.user.IntArea.dto.member;

import com.user.IntArea.entity.enums.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class AdminEditMemberDto {
    private UUID id;
    private Role role;
    private boolean deleted;
}
