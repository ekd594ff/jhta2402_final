package com.user.IntArea.service;

import com.user.IntArea.common.exception.custom.LoginInfoNotFoundException;
import com.user.IntArea.common.exception.custom.UserAlreadyExistsException;
import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.*;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuotationRequestRepository quotationRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUtil imageUtil;
    private final ImageRepository imageRepository;

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

    @Transactional
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

    public MemberWithImagesResponseDto getMemberByEmail() {
        MemberDto memberDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new LoginInfoNotFoundException(new MemberResponseDto()));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new LoginInfoNotFoundException(new MemberResponseDto()));

        List<ImageDto> images = imageRepository.findAllByRefId(member.getId())
                .stream()
                .map(image -> ImageDto.builder()
                        .refId(image.getRefId())
                        .url(image.getUrl())
                        .filename(image.getFilename())
                        .originalFilename(image.getOriginalFilename())
                        .build())
                .collect(Collectors.toList());

        // Member를 MemberResponseDto로 변환
        return new MemberWithImagesResponseDto(member, images);
    }

    @Transactional
    public void updateProfile(UpdateProfileDto updateProfileDto) {
        String email = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 되지 않았습니다.")).getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("updateProfile error"));

        // 사용자 이름 및 비밀번호 업데이트
        member.setUsername(updateProfileDto.getUsername());
        member.setPassword(passwordEncoder.encode(updateProfileDto.getPassword()));

        // 프로필 이미지가 있는 경우 업로드
        if (updateProfileDto.getFile() != null && !updateProfileDto.getFile().isEmpty()) {
            ImageDto imageDto = imageUtil.uploadS3(updateProfileDto.getFile(), member.getId(), 0)
                    .orElseThrow(() -> new NoSuchElementException("S3 오류"));

            imageRepository.deleteByRefId(member.getId());
            imageRepository.save(imageDto.toImage());
        }
    }

    @Transactional
    public void softDeleteMembers(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        memberRepository.softDeleteByIds(ids);
    }

    @Transactional
    public void hardDeleteMembers(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        memberRepository.deleteAllById(ids);
//        quotationRequestRepository.deleteAllById(ids);
    }

    @Transactional
    public void editMemberForAdmin(AdminEditMemberDto adminEditMemberDto) {
        Optional<Member> member = memberRepository.findById(adminEditMemberDto.getId());
        if (member.isPresent()) {
            member.get().setRole(adminEditMemberDto.getRole());
            member.get().setDeleted(adminEditMemberDto.isDeleted());
        } else {
            throw new UsernameNotFoundException("editMember");
        }
    }

    public String getRole() {
        Optional<MemberDto> memberDto = SecurityUtil.getCurrentMember();

        return memberDto.map(MemberDto::getRole).orElse(null);
    }
}
