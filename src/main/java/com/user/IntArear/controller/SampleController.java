package com.user.IntArear.controller;

import com.user.IntArear.entity.Sample;
import com.user.IntArear.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController {

    private final SampleService sampleService;

    @Autowired
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }


    public Long join() {
        //회원가입

        return null;
    }

    public List<Sample> findSample() {
        //전체 회원 조회

        return null;
    }

    public Sample findOne() {
        // 회원 조회
        return null;
    }
}
