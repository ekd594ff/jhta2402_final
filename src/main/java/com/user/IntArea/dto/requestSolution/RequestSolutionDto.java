package com.user.IntArea.dto.requestSolution;

import com.user.IntArea.entity.RequestSolution;

import java.util.UUID;

public class RequestSolutionDto {
    private UUID id;
    private String solutionName;
    private String description;

    public RequestSolutionDto(RequestSolution requestSolution) {
        this.id = requestSolution.getId();
        this.solutionName = requestSolution.getSolution().getTitle();  // 예시 필드
        this.description = requestSolution.getSolution().getDescription();    // 예시 필드
    }
}
