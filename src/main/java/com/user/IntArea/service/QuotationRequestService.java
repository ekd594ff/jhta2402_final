package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationRequestService {

    private final QuotationRequestRepository quotationRequestRepository;
    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public QuotationRequestDto createQuotationRequest(QuotationRequestDto requestDto) {

        // Member 및 Portfolio 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException(""));
        Portfolio portfolio = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new NoSuchElementException(""));

        // QuotationRequest 엔티티 생성 및 저장
        QuotationRequest quotationRequest = QuotationRequest.builder()
                .member(member)
                .portfolio(portfolio)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .build();
        quotationRequestRepository.save(quotationRequest);

        // Solution 엔티티 생성 및 저장
        List<SolutionDto> solutionDtos = requestDto.getSolutions();
        List<Solution> solutions = new ArrayList<>();
        for (SolutionDto solutionDto : solutionDtos) {
            Solution solution = Solution.builder()
                    .title(solutionDto.getTitle())
                    .description(solutionDto.getDescription())
                    .portfolio(portfolio)
                    .price(solutionDto.getPrice())
                    .build();
            solutionRepository.save(solution);

            // RequestSolution 엔티티 생성 및 저장
            RequestSolution requestSolution = RequestSolution.builder()
                    .quotationRequest(quotationRequest)
                    .solution(solution)
                    .build();
            solutions.add(solution);
        }

        // 반환할 DTO 생성
        QuotationRequestDto responseDto = new QuotationRequestDto();
        responseDto.setMemberId(requestDto.getMemberId());
        responseDto.setPortfolioId(requestDto.getPortfolioId());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setDescription(requestDto.getDescription());
        responseDto.setSolutions(solutionDtos);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public QuotationRequestDto getQuotationRequest(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        // DTO 변환
        return QuotationRequestDto.builder()
                .memberId(quotationRequest.getMember().getId())
                .portfolioId(quotationRequest.getPortfolio().getId())
                .title(quotationRequest.getTitle())
                .description(quotationRequest.getDescription())
                .solutions(quotationRequest.getRequestSolutions().stream()
                        .map(rs -> SolutionDto.builder()
                                .title(rs.getSolution().getTitle())
                                .description(rs.getSolution().getTitle())
                                .price(rs.getSolution().getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public QuotationRequestDto updateQuotationRequest(UUID id, QuotationRequestDto requestDto) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        // 업데이트
        quotationRequest.setTitle(requestDto.getTitle());
        quotationRequest.setDescription(requestDto.getDescription());

        // 기존 솔루션 삭제 후 새로운 솔루션 추가
        quotationRequest.getRequestSolutions().clear();
        List<SolutionDto> solutionDtos = requestDto.getSolutions();
        for (SolutionDto solutionDto : solutionDtos) {
            Solution solution = Solution.builder()
                    .title(solutionDto.getTitle())
                    .description(solutionDto.getTitle())
                    .portfolio(quotationRequest.getPortfolio())
                    .price(solutionDto.getPrice())
                    .build();
            solutionRepository.save(solution);

            RequestSolution requestSolution = RequestSolution.builder()
                    .quotationRequest(quotationRequest)
                    .solution(solution)
                    .build();
            quotationRequest.getRequestSolutions().add(requestSolution);
        }
        return requestDto;
    }

    @Transactional
    public void deleteQuotationRequest(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
        quotationRequestRepository.delete(quotationRequest);
    }

    @Transactional(readOnly = true)
    public List<QuotationRequestDto> getAllQuotationRequests() {
        List<QuotationRequest> quotationRequests = quotationRequestRepository.findAll();
        return quotationRequests.stream()
                .map(quotationRequest -> QuotationRequestDto.builder()
                        .memberId(quotationRequest.getMember().getId())
                        .portfolioId(quotationRequest.getPortfolio().getId())
                        .title(quotationRequest.getTitle())
                        .description(quotationRequest.getDescription())
                        .solutions(quotationRequest.getRequestSolutions().stream()
                                .map(rs -> SolutionDto.builder()
                                        .title(rs.getSolution().getTitle())
                                        .description(rs.getSolution().getDescription())
                                        .price(rs.getSolution().getPrice())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        return company;
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
