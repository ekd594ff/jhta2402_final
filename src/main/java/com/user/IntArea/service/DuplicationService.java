package com.user.IntArea.service;

import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DuplicationService {
    private final MemberRepository memberRepository;

    public Boolean isDuplicateUsername(Map<String, String> data) {
        return memberRepository.existsByUsername(data.get("username"));
    }

    public Boolean isDuplicateEmail(Map<String, String> data) {
        return memberRepository.existsByEmail(data.get("email"));
    }
}
