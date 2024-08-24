package com.user.IntArea.controller;

import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.member.MemberRequestDto;
import com.user.IntArea.service.MemberService;
import com.user.IntArea.utils.SecurityUtil;
import com.user.IntArea.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
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
    public ResponseEntity<?> saveMember(@RequestBody MemberRequestDto memberRequestDto) {

        memberService.signup(memberRequestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/info")
    public ResponseEntity<MemberDto> getMemberInfo() {

        return SecurityUtil.getCurrentMember().map(memberDto -> ResponseEntity.ok().body(memberDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
