package com.user.IntArea.dto.solution;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SolutionDto {

    private final String title;
    private final String description;
    private final int price;
}
