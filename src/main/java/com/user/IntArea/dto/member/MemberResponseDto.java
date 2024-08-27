package com.user.IntArea.dto.member;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberResponseDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "영문 숫자조합 8자리 이상 25자 이하로 작성해주세요")
    private String password;
    @NotBlank
    private String username;

    private Platform platform;

    public MemberResponseDto(Member member) {
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.platform = member.getPlatform();
    }
}
