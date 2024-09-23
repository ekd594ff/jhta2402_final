package com.user.IntArea.common.exception;

import com.user.IntArea.common.exception.custom.LoginInfoNotFoundException;
import com.user.IntArea.common.exception.custom.OAuth2UserAlreadyException;
import com.user.IntArea.common.exception.custom.UserAlreadyExistsException;
import com.user.IntArea.dto.member.MemberResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<?> handleDuplicateKeyException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // 로그인 정보가 없을 때, 200번대와 함께 loginInfoDto 반환
    @ExceptionHandler(LoginInfoNotFoundException.class)
    protected ResponseEntity<MemberResponseDto> handleDuplicateKeyException(LoginInfoNotFoundException e) {
        return ResponseEntity.ok().body(e.getMemberResponseDto());
    }

    // OAuth2 로그인 시, 해당 이메일로 가입된 멤버가 다른 플랫폼일 때
    @ExceptionHandler(OAuth2UserAlreadyException.class)
    protected ResponseEntity<?> handleDuplicateKeyException(OAuth2UserAlreadyException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // 본인 포트폴리오 견적신청서 작성
    @ExceptionHandler(IllegalAccessException.class)
    protected ResponseEntity<?> handleDuplicateKeyException(IllegalAccessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
