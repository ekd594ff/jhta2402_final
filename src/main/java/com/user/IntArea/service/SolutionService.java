package com.user.IntArea.service;

import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Solution;
import com.user.IntArea.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    // Create
    public void create(SolutionDto solutionDto) {

        Solution solution = Solution.builder()
                .title(solutionDto.getTitle())
                .description(solutionDto.getDescription())
                .price(solutionDto.getPrice())
                .build();

        solutionRepository.save(solution);
    }

    // Read
    public List<SolutionDto> read() {

        List<Solution> solutions = solutionRepository.findAll();
        List<SolutionDto> solutionDtos = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionDtos.add(new SolutionDto(solution));
        }
        return solutionDtos;
    }

    // Update
    public SolutionDto update(UUID id, SolutionDto solutionDto) {

        Solution existingSolution = solutionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Example not found with id: " + id));

        existingSolution.setTitle(solutionDto.getTitle());
        existingSolution.setDescription(solutionDto.getDescription());

        Solution solution = solutionRepository.save(existingSolution);
        return new SolutionDto(solution);
    }

    // Delete
    @Transactional
    public void delete(UUID id) {

        Solution solution = solutionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Example not found with id: " + id));

        solutionRepository.delete(solution);
    }
}
