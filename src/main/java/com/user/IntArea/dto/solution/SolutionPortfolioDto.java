package com.user.IntArea.dto.solution;

import com.user.IntArea.entity.Solution;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SolutionPortfolioDto {

    private UUID id;
    private String title;
    private String description;
    private int price;

    public SolutionPortfolioDto(Solution solution) {
        this.id = solution.getId();
        this.title = solution.getTitle();
        this.description = solution.getDescription();
        this.price = solution.getPrice();
    }
}
