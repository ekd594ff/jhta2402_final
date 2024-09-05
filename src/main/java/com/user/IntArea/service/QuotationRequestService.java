package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuotationRequestService {

    private final QuotationRequestRepository quotationRequestRepository;
    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;
    private final CompanyRepository companyRepository;

    public QuotationRequestDto createQuotationRequest(QuotationRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Portfolio portfolio = portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        QuotationRequest quotationRequest = QuotationRequest.builder()
                .member(member)
                .portfolio(portfolio)
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





    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        return company;
    }

    // 견적 요청서 작성자 권한

    // (견적요청서를 작성한 사용자 또는 seller) 견적 요청서의 효과를 없앰
    public void invalidateQuotationRequest(QuotationRequest quotationRequest) {
        quotationRequest.setAvailable(false);
        quotationRequestRepository.save(quotationRequest);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 작성한 모든 견적요청서 출력 => QuotationRequestService 로 이동
    public List<QuotationRequest> getAllQuotationRequestOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        return quotationRequestRepository.findAllByMember(member);
    }

    // seller

    // (seller) 회사 관리자가 받은 모든 견적 요청서 출력
    public List<QuotationRequest> getAllQuotationRequestTowardCompany() {
        Company company = getCompanyOfMember();
        return quotationRequestRepository.getAllQuotationRequestTowardCompany(company.getId());
    }


    // admin

    // (admin) 특정 회사가 받은 모든 견적 요청서 출력
    public List<QuotationRequest> getAllQuotationRequestOfCompanyByAdmin(UUID companyId) {
        return quotationRequestRepository.getAllQuotationRequestTowardCompanyByAdmin(companyId);
    }


}
