package com.user.IntArea.controller;

import com.user.IntArea.entity.Solution;
import com.user.IntArea.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
