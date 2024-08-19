package com.user.IntArear.service;

import com.user.IntArear.entity.Sample;
import com.user.IntArear.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public Long join(Sample sample) {
        // 회원가입
        return null;
    }

    public List<Sample> findMembers() {
        // 전체 회원 조회
        return null;
    }

    public Sample findOne(Long SampleId) {
        // 회원 조회
        return null;
    }
}
