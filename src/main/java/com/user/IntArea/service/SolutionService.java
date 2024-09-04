package com.user.IntArea.service;

import com.user.IntArea.entity.Solution;
import com.user.IntArea.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public List<Solution> getSolutionsByPortfolioId(UUID portfolioId) {
        return this.solutionRepository.findAllByPortfolioId(portfolioId);
    }
}
