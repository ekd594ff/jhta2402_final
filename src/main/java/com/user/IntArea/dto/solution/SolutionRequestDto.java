package com.user.IntArea.dto.solution;

import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Solution;
import lombok.Data;

import java.util.UUID;

@Data
public class SolutionRequestDto {

    private UUID id;
    private String title;
    private String description;
    private String price;

    public Solution toSolution(Portfolio portfolio) {
        return Solution.builder()
                .portfolio(portfolio)
                .title(title)
                .description(description)
                .price(Integer.parseInt(price))
                .build();
    }
}
