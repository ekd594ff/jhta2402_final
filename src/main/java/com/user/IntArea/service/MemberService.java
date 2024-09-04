package com.user.IntArea.service;

import com.user.IntArea.common.exception.custom.LoginInfoNotFoundException;
import com.user.IntArea.common.exception.custom.UserAlreadyExistsException;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.*;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
        memberRepository.softDeleteById(member.getId());
    }

    public MemberResponseDto info(UUID uuid) {
        return memberRepository.findById(uuid)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new UsernameNotFoundException("없음"));
    }

    public Page<MemberResponseDto> getMemberList(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberResponseDto::new);
    }

    public Page<MemberResponseDto> getMemberListByFilter(Pageable pageable, Optional<String> filterColumn, Optional<String> filterValue) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "email" -> {
                    return memberRepository.findAllByEmailContains(filterValue.get(), pageable).map(MemberResponseDto::new);
                }
                case "role" -> {
                    return memberRepository.findAllByRole(Role.valueOf(filterValue.get()), pageable).map(MemberResponseDto::new);
                }
                case "username" -> {
                    return memberRepository.findAllByUsernameContains(filterValue.get(), pageable).map(MemberResponseDto::new);
                }
                case "platform" -> {
                    return memberRepository.findAllByPlatformContaining(filterValue.get(), pageable).map(MemberResponseDto::new);
                }
                case "createdAt" -> {
                    return memberRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(MemberResponseDto::new);
                }
                case "updatedAt" -> {
                    return memberRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(MemberResponseDto::new);
                }
                case "deleted" -> {
                    if (filterValue.get().equals("true")) {
                        return memberRepository.findAllByIsDeletedIs(true, pageable).map(MemberResponseDto::new);
                    } else {
                        return memberRepository.findAllByIsDeletedIs(false, pageable).map(MemberResponseDto::new);
                    }
                }
            }
        } else {
            return memberRepository.findAll(pageable).map(MemberResponseDto::new);
        }
        throw new RuntimeException("filterColumn이 잘못됨");
    }

    public MemberResponseDto getMemberByEmail() {
        MemberDto memberDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new LoginInfoNotFoundException(new MemberResponseDto()));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new LoginInfoNotFoundException(new MemberResponseDto()));

        // Member를 MemberResponseDto로 변환하여 반환
        return new MemberResponseDto(member);
    }

}
