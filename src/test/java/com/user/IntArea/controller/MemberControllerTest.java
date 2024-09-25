package com.user.IntArea.controller;

import com.user.IntArea.dto.member.*;
import com.user.IntArea.service.MemberService;
import com.user.IntArea.common.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authentication = mock(Authentication.class);

        // AuthenticationManager 모킹
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
    }

    @Test
    public void testLogin() {
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("test@example.com");
        memberRequestDto.setPassword("asdf1234");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        // Authenticate를 모킹
        when(authenticationManagerBuilder.getObject().authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.getAccessToken(authentication)).thenReturn(ResponseCookie.from("accessToken", "token").build());

        ResponseEntity<?> response = memberController.login(memberRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
    }


    @Test
    public void testLogout() {
        // ResponseCookie 객체를 생성합니다.
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "logoutToken").build();
        ResponseCookie loginCookie = ResponseCookie.from("login", "logoutLogin").build();

        // Map<String, ResponseCookie> 타입으로 수정
        when(tokenProvider.getLogoutToken()).thenReturn(Map.of("accessToken", accessTokenCookie, "login", loginCookie));

        ResponseEntity<?> response = memberController.logout();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
    }


    @Test
    public void testSaveMember() {
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("test@example.com");
        memberRequestDto.setPassword("password");

        ResponseEntity<?> response = memberController.saveMember(memberRequestDto);

        verify(memberService).signup(memberRequestDto);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetMemberInfo() {
        UUID id = UUID.randomUUID();
        MemberResponseDto memberResponseDto = new MemberResponseDto();

        when(memberService.info(id)).thenReturn(memberResponseDto);

        ResponseEntity<MemberResponseDto> response = memberController.getMemberInfo(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(memberResponseDto, response.getBody());
    }

    @Test
    public void testUpdateMember() {
        UpdateMemberDto updateMemberDto = new UpdateMemberDto();

        ResponseEntity<?> response = memberController.updateMember(updateMemberDto);

        verify(memberService).update(updateMemberDto);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteMember() {
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("test@example.com");
        memberRequestDto.setPassword("password");

        ResponseEntity<?> response = memberController.deleteMember(memberRequestDto);

        verify(memberService).delete(memberRequestDto);
        assertEquals(200, response.getStatusCodeValue());
    }

    // 추가적인 테스트 메서드들을 여기에 작성
}
