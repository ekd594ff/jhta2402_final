package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.solution.SolutionDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuotationRequestDto {
    private UUID memberId;
    private UUID portfolioId;
    private String title;
    private String description;
    private List<SolutionDto> solutions;
}
