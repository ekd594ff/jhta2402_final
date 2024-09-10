package com.user.IntArea.service;

import com.user.IntArea.dto.quotationRequest.QuotationRequestCompanyDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final RequestSolutionRepository requestSolutionRepository;


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
                .progress(QuotationProgress.PENDING)
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
            requestSolutionRepository.save(requestSolution);
            solutions.add(solution);
        }

        // 반환할 DTO 생성
        return QuotationRequestDto.builder()
                .memberId(requestDto.getMemberId())
                .portfolioId(requestDto.getPortfolioId())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .solutions(solutionDtos)
                .progress(QuotationProgress.PENDING.name())
                .build();
    }

    @Transactional(readOnly = true)
    public QuotationRequestDto getQuotationRequest(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        return QuotationRequestDto.builder()
                .memberId(quotationRequest.getMember().getId())
                .portfolioId(quotationRequest.getPortfolio().getId())
                .title(quotationRequest.getTitle())
                .description(quotationRequest.getDescription())
                .solutions(quotationRequest.getRequestSolutions().stream()
                        .map(rs -> SolutionDto.builder()
                                .id(rs.getSolution().getId())
                                .title(rs.getSolution().getTitle())
                                .description(rs.getSolution().getDescription())
                                .price(rs.getSolution().getPrice())
                                .createdAt(rs.getSolution().getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .progress(quotationRequest.getProgress().name())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<QuotationRequestDto> getQuotationRequestsByMemberId(UUID memberId, Pageable pageable) {
        Page<QuotationRequest> requests = quotationRequestRepository.findAllByMemberId(memberId, pageable);
        return  requests.map(request -> QuotationRequestDto.builder()
                .memberId(request.getMember().getId())
                .portfolioId(request.getPortfolio().getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .solutions(request.getRequestSolutions().stream()
                        .map(rs -> SolutionDto.builder()
                                .id(rs.getSolution().getId())
                                .title(rs.getSolution().getTitle())
                                .description(rs.getSolution().getDescription())
                                .price(rs.getSolution().getPrice())
                                .createdAt(rs.getSolution().getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .progress(request.getProgress().name())
                .build());
    }

    @Transactional(readOnly = true)
    public Page<QuotationRequestCompanyDto> getQuotationRequestsByCompanyId(UUID companyId, Pageable pageable) {

        // 회사에 속하는 포트폴리오 조회
        Page<Portfolio> portfolios = portfolioRepository.findAllByCompanyId(companyId, pageable);

        // 포트폴리오 ID 리스트 생성
        List<UUID> portfolioIds = portfolios.stream()
                .map(Portfolio::getId)
                .collect(Collectors.toList());

        // 해당 포트폴리오 ID를 가진 견적 신청서 조회
        Page<QuotationRequest> requests = quotationRequestRepository.findAllByPortfolioIds(portfolioIds, pageable);

        // DTO로 변환하여 반환
        return requests.map(request -> QuotationRequestCompanyDto.builder()
                .memberId(request.getMember().getId())
                .portfolioId(request.getPortfolio().getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .solutions(request.getRequestSolutions().stream()
                        .map(rs -> SolutionDto.builder()
                                .id(rs.getSolution().getId())
                                .title(rs.getSolution().getTitle())
                                .description(rs.getSolution().getDescription())
                                .price(rs.getSolution().getPrice())
                                .createdAt(rs.getSolution().getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .progress(request.getProgress().name())
                .companyId(companyId)
                .build());
    }

    @Transactional
    public QuotationRequestDto updateQuotationRequest(UUID id, QuotationRequestDto requestDto) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));

        // QuotationRequest 정보 업데이트
        quotationRequest.setTitle(requestDto.getTitle());
        quotationRequest.setDescription(requestDto.getDescription());
        quotationRequest.setProgress(QuotationProgress.valueOf(requestDto.getProgress())); // progress 추가

        // 기존 솔루션 업데이트 및 새로운 솔루션 추가
        List<SolutionDto> newSolutions = requestDto.getSolutions();

        // 기존 솔루션을 삭제할 경우를 대비하여 솔루션 ID 목록 생성
        Set<UUID> existingSolutionIds = quotationRequest.getRequestSolutions().stream()
                .map(rs -> rs.getSolution().getId())
                .collect(Collectors.toSet());

        // 새로운 솔루션 처리
        for (SolutionDto solutionDto : newSolutions) {
            // 기존 솔루션이 아닌 경우 새로 추가
            if (!existingSolutionIds.contains(solutionDto.getId())) {
                Solution solution = Solution.builder()
                        .title(solutionDto.getTitle())
                        .description(solutionDto.getDescription())
                        .portfolio(quotationRequest.getPortfolio())
                        .price(solutionDto.getPrice())
                        .build();
                solutionRepository.save(solution);

                // RequestSolution 엔티티 생성 및 저장
                RequestSolution requestSolution = RequestSolution.builder()
                        .quotationRequest(quotationRequest)
                        .solution(solution)
                        .build();
                requestSolutionRepository.save(requestSolution);
            } else {
                // 기존 솔루션 업데이트
                Solution existingSolution = solutionRepository.findById(solutionDto.getId())
                        .orElseThrow(() -> new NoSuchElementException(""));
                existingSolution.setTitle(solutionDto.getTitle());
                existingSolution.setDescription(solutionDto.getDescription());
                existingSolution.setPrice(solutionDto.getPrice());
                solutionRepository.save(existingSolution);
            }
        }

        // 반환할 DTO 생성
        return QuotationRequestDto.builder()
                .memberId(requestDto.getMemberId())
                .portfolioId(requestDto.getPortfolioId())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .solutions(newSolutions)
                .progress(requestDto.getProgress())
                .build();
    }

    public void updateProgress () {
        // 견적서 승인 전, 신청서를 취소할 수 있는 API
    }

    @Transactional
    public void deleteQuotationRequest(UUID id) {
        quotationRequestRepository.deleteById(id);
    }
}
