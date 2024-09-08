package com.user.IntArea.service;

import com.user.IntArea.repository.RequestSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestSolutionService {

    private final RequestSolutionRepository requestSolutionRepository;


}
