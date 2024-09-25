package com.user.IntArea.common.utils;

import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.service.CustomUserDetailsService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SecurityUtil {

    private final CustomUserDetailsService userDetailsService;

    public static Optional<MemberDto> getCurrentMember() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return Optional.empty();
        }

        String role = String.valueOf(userDetails.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다. 다시 시도해주세요.")));

        return Optional.of(MemberDto.builder()
                .email(userDetails.getUsername())
                .role(role)
                .build());
    }

    // 회사 생성 후, ROLE_SELLER 로 변경된 인증정보로 업데이트
    public Authentication updateAuthenticationRole() {
        String email = getCurrentMember().map(MemberDto::getEmail).orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다."));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}