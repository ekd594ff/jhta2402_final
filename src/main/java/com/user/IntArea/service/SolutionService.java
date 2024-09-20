package com.user.IntArea.service;

import com.user.IntArea.dto.requestSolution.RequestSolutionDto;
import com.user.IntArea.dto.solution.SolutionForQuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionWithImageDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.RequestSolution;
import com.user.IntArea.entity.Solution;
import com.user.IntArea.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public List<Solution> getSolutionsByPortfolioId(UUID portfolioId) {
        return this.solutionRepository.findAllByPortfolioId(portfolioId);
    }

    public List<SolutionForQuotationRequestDto> getSolutionListFor(QuotationRequest quotationRequest) {
        List<Solution> solutions = solutionRepository.getSolutionsByQuotationRequestId(quotationRequest.getId());
        List<SolutionForQuotationRequestDto> solutionForQuotationRequestDtos = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionForQuotationRequestDtos.add(new SolutionForQuotationRequestDto(solution));
        }
        return solutionForQuotationRequestDtos;
    }

    public List<SolutionWithImageDto> getTopMostQuotedSolutions(int count) {
        return solutionRepository.getTopMostQuotedSolutions(count)
                .stream().map(SolutionWithImageDto::new).toList();
    }
}
