package com.user.IntArea.controller;

import com.user.IntArea.dto.solution.SolutionWithImageDto;
import com.user.IntArea.entity.Solution;
import com.user.IntArea.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<?> getSolutionsByPortfolioId(@PathVariable(name = "id") UUID portfolioId) {
        return ResponseEntity.ok(this.solutionService.getSolutionsByPortfolioId(portfolioId).stream().map(Solution::toSolutionDto));
    }

    @GetMapping("/list/top8")
    public ResponseEntity<List<SolutionWithImageDto>> getTop8MostQuotedSolutions() {
        List<SolutionWithImageDto> solutionWithImageDtos = solutionService.getTop8MostQuotedSolutions();
        return ResponseEntity.ok().body(solutionWithImageDtos);
    }
}
