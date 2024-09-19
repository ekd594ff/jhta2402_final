package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.QuotationRequest;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestDto {
    private UUID memberId;
    private UUID portfolioId;
    private String title;
    private String description;
    private List<SolutionDto> solutions;
    private String progress;
    private String username;

    // QuotationRequest를 받아서 필드를 매핑하는 생성자 추가
    public QuotationRequestDto(QuotationRequest quotationRequest) {
        this.memberId = quotationRequest.getMember().getId();
        this.portfolioId = quotationRequest.getPortfolio().getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress().toString();

        // RequestSolution을 SolutionDto로 변환하여 리스트에 추가
        this.solutions = quotationRequest.getRequestSolutions().stream()
                .map(requestSolution -> new SolutionDto(requestSolution.getSolution()))
                .collect(Collectors.toList());
    }
}
