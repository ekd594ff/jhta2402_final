package com.user.IntArea.controller;

import com.user.IntArea.dto.solution.SolutionDto;
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

    // Create
    @PostMapping("/create")
    public SolutionDto create(@RequestBody SolutionDto solutionDto) {

        return solutionService.create(solutionDto);
    }

    // Read
    @GetMapping("/{id}")
    public List<SolutionDto> readAllSolutions() {
        return solutionService.read();
    }

    // Update
    @PutMapping("/{id}")
    public SolutionDto update(@PathVariable UUID id, @RequestBody SolutionDto solutionDto) {
        return solutionService.update(id, solutionDto);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        solutionService.delete(id);
    }

}
