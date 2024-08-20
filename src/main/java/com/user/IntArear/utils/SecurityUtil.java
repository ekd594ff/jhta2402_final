package com.user.IntArear.utils;

import com.user.IntArear.dto.MemberResponseDto;
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

    public static Optional<MemberResponseDto> getCurrentMember() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = String.valueOf(userDetails.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return Optional.of(MemberResponseDto.builder()
                .email(userDetails.getUsername())
                .role(role)
                .build());
    }
}