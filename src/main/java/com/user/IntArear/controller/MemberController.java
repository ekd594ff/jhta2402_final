package com.user.IntArear.controller;

import com.user.IntArear.dto.MemberRequestDto;
import com.user.IntArear.dto.MemberResponseDto;
import com.user.IntArear.service.MemberService;
import com.user.IntArear.utils.SecurityUtil;
import com.user.IntArear.utils.jwt.TokenProvider;
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

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody MemberRequestDto memberRequestDto) {

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
    public ResponseEntity<String> saveMember(@RequestBody MemberRequestDto memberRequestDto) {

        memberService.signup(memberRequestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/info")
    public ResponseEntity<MemberResponseDto> getMemberInfo() {

        Optional<MemberResponseDto> optionalMemberResponseDto = SecurityUtil.getCurrentMember();

        return optionalMemberResponseDto.map(memberResponseDto ->
                        ResponseEntity.ok().body(memberResponseDto))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
