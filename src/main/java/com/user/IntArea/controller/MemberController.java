package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.service.MemberService;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.common.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        ResponseCookie accessToken = tokenProvider.createAccessToken(authentication);
        ResponseCookie loginToken = tokenProvider.getLoginToken();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                .header(HttpHeaders.SET_COOKIE, loginToken.toString())
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

    @PutMapping()
    public ResponseEntity<?> updateMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        log.info("memberResponseDto={}", memberRequestDto);
        memberService.update(memberRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.delete(memberRequestDto);
        return ResponseEntity.ok().build();
    }
}
