package com.user.IntArea.common.exception.custom;

import com.user.IntArea.dto.member.MemberResponseDto;
import lombok.Getter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public class LoginInfoNotFoundException extends UsernameNotFoundException {

    private final MemberResponseDto memberResponseDto;

    public LoginInfoNotFoundException(MemberResponseDto memberResponseDto) {
        super("로그인 정보가 없습니다.");
        this.memberResponseDto = memberResponseDto;
    }

}