package com.user.IntArea.common.utils;

import com.user.IntArea.dto.member.MemberDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    public static Optional<MemberDto> getCurrentMember() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String role = String.valueOf(userDetails.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다. 다시 시도해주세요.")));

        return Optional.of(MemberDto.builder()
                .email(userDetails.getUsername())
                .role(role)
                .build());
    }
}