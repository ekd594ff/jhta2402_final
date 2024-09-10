package com.user.IntArea.service;

import com.user.IntArea.dto.requestSolution.RequestSolutionDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.RequestSolution;
import com.user.IntArea.repository.RequestSolutionRepository;
import com.user.IntArea.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestSolutionService {

    private final RequestSolutionRepository requestSolutionRepository;

}
