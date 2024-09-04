package com.user.IntArea.config;

import com.user.IntArea.common.jwt.JwtSecurityConfig;
import com.user.IntArea.common.jwt.TokenProvider;
import com.user.IntArea.common.jwt.error.JwtAccessDeniedHandler;
import com.user.IntArea.common.jwt.error.JwtAuthenticationEntryPoint;
import com.user.IntArea.common.oauth.CustomOAuth2FailureHandler;
import com.user.IntArea.common.oauth.CustomOAuth2SuccessHandler;
import com.user.IntArea.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 인증이 필요한 Get api
        String[] companyGetAuth = {};
        String[] imageGetAuth = {};
        String[] memberGetAuth = {"/api/member/info", "/api/member/role"};
        String[] portfolioGetAuth = {};
        String[] quotationGetAuth = {};
        String[] quotationRequestGetAuth = {};
        String[] reportGetAuth = {};
        String[] requestSolutionGetAuth = {};
        String[] reviewGetAuth = {};
        String[] solutionGetAuth = {};

        // 인증이 필요하지 않은 Get이 아닌 api
        String[] companyApi = {};
        String[] imageApi = {};
        String[] memberApi = {"/api/member/login", "/api/member/signup", "/api/oauth2/**"};
        String[] duplicationApi = {"/api/duplication/email", "/api/duplication/username"};
        String[] portfolioApi = {};
        String[] quotationApi = {};
        String[] quotationRequestApi = {};
        String[] reportApi = {};
        String[] requestSolutionApi = {};
        String[] reviewApi = {};
        String[] solutionApi = {};

        // SELLER 권한이 필요한 api
        String[] sellerApi = {"/api/*/seller/**"};

        // ADMIN 권한이 필요한 api
        String[] adminApi = {"/api/*/admin/**"};

        http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        // 인증이 필요한 Get api
                        .requestMatchers(HttpMethod.GET, companyGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, imageGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, memberGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, portfolioGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, quotationGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, quotationRequestGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, reportGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, requestSolutionGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, reviewGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, solutionGetAuth).authenticated()
                        .requestMatchers(HttpMethod.GET, sellerApi).hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, adminApi).hasRole("ADMIN")

                        // 나머지 Get 요청 허용 (페이지 이동은 React에서 관리)
                        .requestMatchers(HttpMethod.GET).permitAll()

                        // 인증이 필요하지 않은 Get이 아닌 api
                        .requestMatchers(companyApi).permitAll()
                        .requestMatchers(imageApi).permitAll()
                        .requestMatchers(memberApi).permitAll()
                        .requestMatchers(duplicationApi).permitAll()
                        .requestMatchers(portfolioApi).permitAll()
                        .requestMatchers(quotationApi).permitAll()
                        .requestMatchers(quotationRequestApi).permitAll()
                        .requestMatchers(reportApi).permitAll()
                        .requestMatchers(requestSolutionApi).permitAll()
                        .requestMatchers(reviewApi).permitAll()
                        .requestMatchers(solutionApi).permitAll()

                        // SELLER 권한이 필요한 api
                        .requestMatchers(sellerApi).hasRole("SELLER")

                        // ADMIN 권한이 필요한 api
                        .requestMatchers(adminApi).hasRole("ADMIN")

                        // 나머지 요청 인증 필요
                        .anyRequest().authenticated()
                )

                // token을 사용하는 방식이기 때문에 csrf disable
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {
                })

                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/api/login/oauth2/code/*"))
                        .userInfoEndpoint(userinfo -> userinfo
                                .userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler))

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );

        return http.build();
    }
}