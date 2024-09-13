package com.user.IntArea.common.oauth;

import com.user.IntArea.common.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${client.url}")
    String clientUrl;

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.addCookie(tokenProvider.getAccessServletCookie(authentication));

        // http://localhost:808? 를 얻기 위한 코드 (개발 편의를 위한 코드, 배포 시 제거 필요)
        String requestUrl = request.getRequestURL().substring(0, 21);

        // 이후 response.sendRedirect(clientUrl); 로 변경
        response.sendRedirect((!requestUrl.startsWith("http://localhost:808")) ? clientUrl : requestUrl);
    }
}
