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
    @Email(message = "이메일 형식에 맞춰주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "영문 숫자조합 8자리 이상 25자 이하로 작성해주세요")
    private String password;
    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    private Platform platform;
}
