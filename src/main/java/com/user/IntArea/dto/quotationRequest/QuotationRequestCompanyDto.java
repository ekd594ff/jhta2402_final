package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.member.QuotationRequestMemberDto;
import com.user.IntArea.dto.solution.SolutionDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestCompanyDto {
    private UUID id;
    private QuotationRequestMemberDto member;
    private UUID portfolioId;
    private String title;
    private String description;
    private List<SolutionDto> solutions;
    private String progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID companyId;
}
