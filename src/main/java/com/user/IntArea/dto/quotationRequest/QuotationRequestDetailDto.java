package com.user.IntArea.dto.quotationRequest;

import com.user.IntArea.dto.member.QuotationRequestMemberDto;
import com.user.IntArea.dto.quotation.QuotationDetailDto;
import com.user.IntArea.dto.solution.SolutionDetailDto;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuotationRequestDetailDto {

    private UUID id;
    private String title;
    private String description;
    private QuotationProgress progress;
    private String loginEmail;
    private QuotationRequestMemberDto member;
    private List<SolutionDetailDto> solutions;
    private List<QuotationDetailDto> quotations;

    @Builder
    public QuotationRequestDetailDto(
            QuotationRequest quotationRequest,
            String loginEmail,
            String memberUrl,
            List<SolutionDetailDto> solutions,
            List<QuotationDetailDto> quotations) {
        this.id = quotationRequest.getId();
        this.title = quotationRequest.getTitle();
        this.description = quotationRequest.getDescription();
        this.progress = quotationRequest.getProgress();
        this.loginEmail = loginEmail;
        this.member = new QuotationRequestMemberDto(quotationRequest.getMember(), memberUrl);
        this.solutions = solutions;
        this.quotations = quotations;
    }
}
