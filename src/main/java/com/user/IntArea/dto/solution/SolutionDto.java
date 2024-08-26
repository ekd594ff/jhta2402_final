package com.user.IntArea.dto.solution;

import com.user.IntArea.entity.Solution;
import lombok.*;

@Data
@AllArgsConstructor
public class SolutionDto {

    private String title;
    private String description;
    private int price;



    public SolutionDto(Solution solution) {
        this.title = solution.getTitle();
        this.description = solution.getDescription();
        this.price = solution.getPrice();
    }
}
