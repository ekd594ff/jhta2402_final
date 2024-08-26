package com.user.IntArea.dto.solution;

import com.user.IntArea.entity.Solution;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SolutionDto {

    private final String title;
    private final String description;
    private final String portfolio;
    private final String price;

}
