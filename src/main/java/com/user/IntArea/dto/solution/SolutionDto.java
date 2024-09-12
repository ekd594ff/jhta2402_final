package com.user.IntArea.dto.solution;

import com.user.IntArea.entity.Solution;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SolutionDto {
    private final String title;
    private final String description;
    private final int price;
    private final UUID id;
    private LocalDateTime createdAt;

    public SolutionDto(Solution solution) {
        this.title = solution.getTitle();
        this.description = solution.getDescription();
        this.price = solution.getPrice();
        this.id = solution.getId();
        this.createdAt = solution.getCreatedAt();
    }

    @Builder
    public SolutionDto(String title, String description, int price, UUID id, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.id = id;
        this.createdAt = createdAt;
    }
}
