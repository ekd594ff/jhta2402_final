package com.user.IntArea.dto.portfolio;

import lombok.Data;

import java.util.UUID;

@Data
public class PortfolioUpdateDto {

    private UUID id;
    private String title;
    private String description;
//    private List<Solution> solution;
}
