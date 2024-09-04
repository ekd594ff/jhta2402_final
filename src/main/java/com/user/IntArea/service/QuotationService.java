package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.portfolio.PortfolioCreateDto;
import com.user.IntArea.dto.quotation.QuotationCreateDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.CompanyRepository;
import com.user.IntArea.repository.MemberRepository;
import com.user.IntArea.repository.QuotationRepository;
import com.user.IntArea.repository.QuotationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final QuotationRequestRepository quotationRequestRepository;


    // seller

    // 견적서에 접근하는 멤버가 그 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void isCompanyManager(Quotation quotation) {
        Portfolio portfolio = quotation.getQuotationRequest().getPortfolio();
        if(portfolio == null) {
            throw new NoSuchElementException("포트폴리오가 존재하지 않습니다.");
        }
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        if (!company.equals(portfolio.getCompany())) {
            throw new NoSuchElementException("");
        }
    }

    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        return company;
    }

    private boolean isLoggedMemberQuotationRequestWriter(QuotationRequest quotationRequest) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        return member.equals(quotationRequest.getMember());
    }


    // 일반 권한


    // (견적요청서를 작성한 사용자 권한) 사용자가 작성한 모든 견적요청서 출력 => QuotationRequestService 로 이동
    public List<QuotationRequest> getAllQuotationRequestOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        return quotationRequestRepository.findAllByMember(member);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 선택한 특정 견적요청서에 대해서 작성된 견적서 출력
    public Quotation getQuotationForQuotationRequest(QuotationRequest quotationRequest) {
        if(!isLoggedMemberQuotationRequestWriter(quotationRequest)) {
            throw new NoSuchElementException("잘못된 접근입니다.");
        }
        Quotation quotation = quotationRequest.getWrittenQuotation();
        if(quotation == null) {
            throw new NoSuchElementException("업체가 작성된 견적서가 없습니다.");
        }
        return quotation;
    }

    // seller

    // (seller) 견적서 생성 - 받은 견적 요청서와 연관된 견적서를 작성
    public Quotation create(QuotationCreateDto quotationCreateDto) {
        Quotation quotation = Quotation.builder()
                .totalTransactionAmount(quotationCreateDto.getTotalTransactionAmount())
                .quotationRequest(quotationCreateDto.getQuotationRequestId())
                .build();
        return quotationRepository.save(quotation);
    }


    // (seller) 견적서 수정 - 견적서를 새로 생성하고 견적요청서와 연관관계를 맺음. 기존의 견적서는 isAvailable = false 처리


    // (seller) 거래 불가 견적서 발송 (견적 요청서에 대해 '견적 불가' 내용이 담긴 견적서 발송. 발송 후 해당 요청서에 대해서 판매자의 견적서 작성 권한 박탈)

    /*예시
     * title: 견적 불가
     * description: 사유: 서비스 불가 지역, 서비스 종료, 서비스를 위한 물품 재고 부족, 휴업 및 폐업, 기타 */


    // (seller) 회사 관리자가 받은 모든 견적 요청서 출력 => QuotationRequestService에서 작성

    // (seller) 회사 관리자가 작성한 모든 견적서 출력


    // admin

    // (admin) 특정 회사가 받은 모든 견적 요청서 출력 => QuotationRequestService에서 작성
    // (admin) 특정 회사가 작성한 모든 견적서 출력

}
