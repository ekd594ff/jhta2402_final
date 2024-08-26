package com.user.IntArea.service;

import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Solution;
import com.user.IntArea.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    // Create
    public void create(SolutionDto solutionDto) {

        Solution solution = Solution.builder()
                .title(solutionDto.getTitle())
                .description(solutionDto.getDescription())
                .portfolio(null)
                .price(Integer.parseInt(solutionDto.getPrice()))
                .build();

        solutionRepository.save(solution);
    }

    // Read
    public void read() {


    }

    // Update
    public void update() {


    }

    // Delete
    public void delete() {

    }
}
