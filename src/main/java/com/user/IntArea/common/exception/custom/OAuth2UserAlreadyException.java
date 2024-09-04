package com.user.IntArea.common.exception.custom;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class OAuth2UserAlreadyException extends OAuth2AuthenticationException {

    public OAuth2UserAlreadyException(String message) {
        super(message);
    }

}