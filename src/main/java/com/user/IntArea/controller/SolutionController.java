package com.user.IntArea.controller;

import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SolutionDto solutionDto) {

        solutionService.create(solutionDto);

        return ResponseEntity.ok().build();
    }


}
