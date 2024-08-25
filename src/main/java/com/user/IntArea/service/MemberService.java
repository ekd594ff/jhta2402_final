package com.user.IntArea.service;

import com.user.IntArea.common.exception.custom.UserAlreadyExistsException;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberRequestDto memberRequestDto) {

        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .username(UUID.randomUUID().toString()) // todo : 이름 받아서 변경
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .role(Role.ROLE_USER)
                .platform(Platform.SERVER) // todo : 플랫폼 받아서 변경
                .build();

        memberRepository.save(member);
    }
}
