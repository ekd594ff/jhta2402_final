package com.user.IntArea.service;

import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.MemberRepository;
import com.user.IntArea.repository.PortfolioRepository;
import com.user.IntArea.repository.QuotationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationRequestService {

    private final QuotationRequestRepository quotationRequestRepository;
    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;

    public QuotationRequestDto createQuotationRequest(QuotationRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Portfolio portfolio = portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        QuotationRequest quotationRequest = QuotationRequest.builder()
                .member(member)
//                .portfolio(portfolio)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();

        QuotationRequest savedRequest = quotationRequestRepository.save(quotationRequest);

        return convertToDto(savedRequest);
    }

    private QuotationRequestDto convertToDto(QuotationRequest quotationRequest) {
        return QuotationRequestDto.builder()
                .memberId(quotationRequest.getMember().getId())
                .portfolioId(quotationRequest.getPortfolio().getId())
                .title(quotationRequest.getTitle())
                .description(quotationRequest.getDescription())
                .build();
    }
}
