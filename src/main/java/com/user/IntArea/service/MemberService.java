package com.user.IntArea.service;

import com.user.IntArea.common.exception.custom.UserAlreadyExistsException;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.member.UpdateMemberDto;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberRequestDto memberRequestDto) {

        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .username(memberRequestDto.getUsername()) // todo : 이름 받아서 변경
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .role(Role.ROLE_USER)
                .platform(Platform.SERVER) // todo : 플랫폼 받아서 변경
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public void update(UpdateMemberDto updateMemberDto) {
        String email = SecurityUtil.getCurrentMember().get().getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("없음"));
        member.setUsername(updateMemberDto.getUsername());
        member.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));
    }

    @Transactional
    public void delete(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findById(memberRequestDto.getId()).orElseThrow(() -> new UsernameNotFoundException("없음"));
        if (!member.getEmail().equals(memberRequestDto.getEmail())) {
            throw new UsernameNotFoundException("아이디 비밀번호가 틀렸습니다.");
        }
        if (passwordEncoder.matches(member.getPassword(), memberRequestDto.getPassword())) {
            throw new UsernameNotFoundException("아이디 비밀번호가 틀렸습니다");
        }
        memberRepository.delete(member);
    }

    public MemberResponseDto info(UUID uuid) {
        return memberRepository.findById(uuid)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));
    }
}
