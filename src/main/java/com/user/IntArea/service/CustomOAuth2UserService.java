package com.user.IntArea.service;

import com.user.IntArea.common.exception.custom.OAuth2UserAlreadyException;
import com.user.IntArea.common.oauth.CustomOAuth2User;
import com.user.IntArea.common.oauth.dto.*;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        switch (registrationId) {
            case "naver" -> oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            default -> {
                return null;
            }
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2Response.getEmail());
        Member member;
        if (optionalMember.isEmpty()) {

            String username = memberRepository.findByUsername(oAuth2Response.getName()).isEmpty()
                    ? oAuth2Response.getName() : oAuth2Response.getName() + "_" + UUID.randomUUID().toString().substring(0, 4);

            // 회원 정보가 없는 경우 저장
            member = Member.builder()
                    .email(oAuth2Response.getEmail())
                    .username(username)
                    .password(UUID.randomUUID().toString())
                    .role(Role.ROLE_USER)
                    .platform(oAuth2Response.getPlatform())
                    .build();

            memberRepository.save(member);
        } else {

            member = optionalMember.get();

            if (!member.getPlatform().equals(oAuth2Response.getPlatform())) {

                // 플랫폼이 같지 않은 경우 -> 중복 회원가입을 막기 위해 throw Exception
                throw new OAuth2UserAlreadyException(member.getPlatform().getPlatform());
            }

            memberRepository.save(member);
        }

        MemberOAuth2Dto memberOAuth2Dto = MemberOAuth2Dto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .role(member.getRole())
                .build();

        return new CustomOAuth2User(memberOAuth2Dto);
    }
}
