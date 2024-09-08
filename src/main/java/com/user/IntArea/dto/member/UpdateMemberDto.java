package com.user.IntArea.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMemberDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "영문 숫자조합 8자리 이상 25자 이하로 작성해주세요")
    private String password;
    @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,16}$", message = "이름을 입력해주세요")
    private String username;

    public void setProfileImageUrl(String url) {
    }
}
