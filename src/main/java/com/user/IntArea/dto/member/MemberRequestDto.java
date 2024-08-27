package com.user.IntArea.dto.member;

import com.user.IntArea.entity.enums.Platform;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.UUID;

@Data
public class MemberRequestDto {

    private UUID id;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "영문 숫자조합 8자리 이상")
    private String password;

    @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,16}$",message = "2글자 이상 16글자 이하로 작성해라")
    private String username;

    private Platform platform;
}
