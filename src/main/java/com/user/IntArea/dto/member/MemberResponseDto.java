package com.user.IntArea.dto.member;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MemberResponseDto {

    private UUID id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식으로 입력해주세요")
    private String email;

    private Role role;
    @NotBlank
    @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,16}$",message = "2글자 이상 16글자 이하로 작성해라")
    private String username;

    private Platform platform;

    private boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.role = member.getRole();
        this.isDeleted = member.isDeleted();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.platform = member.getPlatform();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}
