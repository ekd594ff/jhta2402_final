package com.user.IntArea.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.dto.member.UpdateMemberDto;
import com.user.IntArea.dto.member.UpdateProfileDto;
import com.user.IntArea.service.MemberService;
import com.user.IntArea.common.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie accessToken = tokenProvider.getAccessToken(authentication);
        ResponseCookie loginToken = tokenProvider.getLoginToken();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                .header(HttpHeaders.SET_COOKIE, loginToken.toString())
                .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        Map logoutCookieMap = tokenProvider.getLogoutToken();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, logoutCookieMap.get("accessToken").toString())
                .header(HttpHeaders.SET_COOKIE, logoutCookieMap.get("login").toString())
                .build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        memberService.signup(memberRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@RequestParam UUID id) {
        return ResponseEntity.ok().body(memberService.info(id));
    }

    // SecurityUtil의 이메일 정보를 통해 유저 정보를 조회하는 메서드
    @GetMapping("/email")
    public ResponseEntity<MemberResponseDto> getMemberByEmail() {
        MemberResponseDto memberResponseDto = memberService.getMemberByEmail();
        return ResponseEntity.ok().body(memberResponseDto);
    }

    @PatchMapping()
    public ResponseEntity<?> updateMember(@Valid @RequestBody UpdateMemberDto updateMemberDto) {
        log.info("memberResponseDTO={}", updateMemberDto);
        memberService.update(updateMemberDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid UpdateProfileDto updateProfileDto) {
        log.info("Updating profile for member: {}", updateProfileDto.getUsername());
        memberService.updateProfile(updateProfileDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.delete(memberRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role")
    public ResponseEntity<?> getLogin() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/role")
    public ResponseEntity<?> getAdminRole() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<MemberResponseDto>> getMemberList(@RequestParam int page, @RequestParam(name = "pageSize") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MemberResponseDto> memberResponseDtoPage = memberService.getMemberList(pageable);
        return ResponseEntity.ok().body(memberResponseDtoPage);
    }

    @GetMapping("/admin/list/filter/contains")
    public ResponseEntity<Page<MemberResponseDto>> getSearchMember(@RequestParam int page, @RequestParam(name = "pageSize") int size,
                                                                   @RequestParam(defaultValue = "createdAt",required = false) String sortField,
                                                                   @RequestParam(defaultValue = "desc",required = false) String sort,
                                                                   @RequestParam(required = false) String filterColumn,
                                                                   @RequestParam(required = false) String filterValue) {
        log.info("sortField={}",sortField);
        log.info("sort={}",sort);
        log.info("filterColumn={}",filterColumn);
        System.out.println(filterColumn);
        log.info("filterValue={}",filterValue);
        System.out.println(filterValue);

        Optional<String> filterColumnOptional = Optional.ofNullable(filterColumn);
        Optional<String> filterValueOptional = Optional.ofNullable(filterValue);

        Pageable pageable;
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortField).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        }
        Page<MemberResponseDto> memberResponseDtoPage = memberService.getMemberListByFilter(pageable, filterColumnOptional, filterValueOptional);
        return ResponseEntity.ok().body(memberResponseDtoPage);
    }

    @GetMapping("/seller/role")
    public ResponseEntity<?> getSellerRole() {
        return ResponseEntity.ok().build();
    }
}
