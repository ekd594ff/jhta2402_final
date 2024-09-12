package com.user.IntArea.common.oauth;

import com.user.IntArea.common.exception.custom.OAuth2UserAlreadyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${client.url}")
    String clientUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        OAuth2UserAlreadyException oAuth2UserAlreadyException = (OAuth2UserAlreadyException) exception;

        // http://localhost:808? 를 얻기 위한 코드 (개발 편의를 위한 코드, 배포 시 제거 필요)
        String requestUrl = request.getRequestURL().substring(0, 21);

        // 이후 response.sendRedirect(clientUrl); 로 변경
        response.sendRedirect((!requestUrl.startsWith("http://localhost:808"))
                ? clientUrl + "/login?error=" + oAuth2UserAlreadyException.getError().getErrorCode()
                : requestUrl + "/login?error=" + oAuth2UserAlreadyException.getError().getErrorCode());
    }
}
