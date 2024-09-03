package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.solution.SolutionPortfolioDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationRequestDto {
    private UUID memberId;
    private UUID portfolioId;
    private String title;
    private String description;
}
