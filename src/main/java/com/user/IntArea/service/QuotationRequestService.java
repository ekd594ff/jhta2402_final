package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.member.QuotationRequestMemberDto;
import com.user.IntArea.dto.quotation.QuotationDetailDto;
import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotationRequest.*;
import com.user.IntArea.dto.solution.SolutionDetailDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.dto.solution.SolutionForQuotationRequestDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.entity.enums.QuotationProgress;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final CompanyRepository companyRepository;

    private final QuotationService quotationService;
    private final SolutionService solutionService;
    private final ImageRepository imageRepository;
    private final QuotationRepository quotationRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public QuotationRequestDto createQuotationRequest(QuotationRequestDto requestDto) throws IllegalAccessException {

        // Member 및 Portfolio 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("멤버 정보가 없습니다."));
        Portfolio portfolio = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new NoSuchElementException("포트폴리오 정보가 없습니다."));

        // 본인 포트폴리오인 경우 생성 불가
        if (portfolio.getCompany().getMember().getEmail().equals(member.getEmail())) {
            throw new IllegalAccessException("본인 포트폴리오에 견적신청서를 넣을 수 없습니다.");
        }

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
        for (SolutionDto solutionDto : solutionDtos) {
            Solution solution = solutionRepository
                    .findById(solutionDto.getId())
                    .orElseThrow(() -> new NoSuchElementException("no such solution"));
            solutionRepository.save(solution);

            // RequestSolution 엔티티 생성 및 저장
            RequestSolution requestSolution = RequestSolution.builder()
                    .quotationRequest(quotationRequest)
                    .solution(solution)
                    .build();
            requestSolutionRepository.save(requestSolution);
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
                .orElseThrow(() -> new NoSuchElementException("견적신청서 정보가 없습니다."));

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

    public QuotationRequestDetailDto getQuotationRequestDetail(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("견적신청서 정보가 없습니다."));

        String loginEmail = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다."))
                .getEmail();
        // 신청 유저, 회사 유저만 api 허용
        if (!loginEmail.equals(quotationRequest.getMember().getEmail())) {
            Company company = getCompanyOfMember();
            if (!loginEmail.equals(company.getMember().getEmail())) {
                throw new UsernameNotFoundException("접근 권한이 없습니다.");
            }
        }

        String memberUrl = imageRepository.findByRefId(quotationRequest.getMember().getId())
                .orElseGet(Image::new).getUrl();
        List<SolutionDetailDto> solutions = solutionRepository.getSolutionsByQuotationRequestId(quotationRequest.getId())
                .stream().map(SolutionDetailDto::new).toList();
        List<QuotationDetailDto> quotations = quotationRepository.findAllByQuotationRequestIdOrderByUpdatedAt(quotationRequest.getId())
                .stream().map(quotation -> {
                    List<String> quotationUrls = imageRepository.findAllByRefId(quotation.getId())
                            .stream().map(Image::getUrl).toList();
                    return new QuotationDetailDto(quotation, quotationUrls);
                })
                .toList();

        return QuotationRequestDetailDto.builder()
                .quotationRequest(quotationRequest)
                .loginEmail(loginEmail)
                .memberUrl(memberUrl)
                .solutions(solutions)
                .quotations(quotations)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<QuotationRequestListDto> getQuotationRequestsByMemberId(String progress, Pageable pageable) {
        String email = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다."))
                .getEmail();
        UUID memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 멤버가 없습니다."))
                .getId();

        List<QuotationProgress> progresses = stringToProgress(progress);

        Page<QuotationRequest> requests = quotationRequestRepository.findAllByMemberIdAndProgresses(memberId, progresses, pageable);

        return requests.map(request -> QuotationRequestListDto.builder()
                .quotationRequest(request)
                .memberId(request.getMember().getId())
                .portfolio(request.getPortfolio())
                .portfolioUrl(imageRepository.findFirstByRefId(request.getPortfolio().getId()).orElse(new Image()).getUrl())
                .solutions(request.getRequestSolutions().stream()
                        .map(rs -> SolutionDto.builder()
                                .id(rs.getSolution().getId())
                                .title(rs.getSolution().getTitle())
                                .description(rs.getSolution().getDescription())
                                .price(rs.getSolution().getPrice())
                                .createdAt(rs.getSolution().getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .review(reviewRepository.findByQuotationRequestId(request.getId()).orElseGet(Review::new))
                .build());
    }

    @Transactional(readOnly = true)
    public Page<QuotationRequestCompanyDto> getQuotationRequestsByCompanyId(String progress, Pageable pageable) {

        // progress : PENDING, APPROVED, ALL
        List<QuotationProgress> progresses = stringToProgress(progress);

        Company company = getCompanyOfMember();

        // 회사에 속하는 포트폴리오 조회
        List<Portfolio> portfolios = portfolioRepository.findAllByCompanyId(company.getId());

        // 포트폴리오 ID 리스트 생성
        List<UUID> portfolioIds = portfolios.stream()
                .map(Portfolio::getId)
                .collect(Collectors.toList());

        // 해당 포트폴리오 ID와 Progress를 가진 견적 신청서 조회
        Page<QuotationRequest> requests = quotationRequestRepository.findAllByPortfolioIdsAndProgress(portfolioIds, progresses, pageable);

        // DTO로 변환하여 반환
        return requests.map(request -> {
            String memberUrl = imageRepository.findByRefId(request.getMember().getId()).orElseGet(Image::new).getUrl();

            return QuotationRequestCompanyDto.builder()
                    .quotationRequest(request)
                    .memberUrl(memberUrl)
                    .portfolioUrl(imageRepository.findFirstByRefId(request.getPortfolio().getId()).orElse(new Image()).getUrl())
                    .solutions(request.getRequestSolutions().stream()
                            .map(rs -> SolutionDto.builder()
                                    .id(rs.getSolution().getId())
                                    .title(rs.getSolution().getTitle())
                                    .description(rs.getSolution().getDescription())
                                    .price(rs.getSolution().getPrice())
                                    .createdAt(rs.getSolution().getCreatedAt())
                                    .build())
                            .collect(Collectors.toList()))
                    .companyId(company.getId())
                    .review(reviewRepository.findByQuotationRequestId(request.getId()).orElseGet(Review::new))
                    .build();
        });
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

    @Transactional
    public void cancelQuotationRequest(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("견적 요청을 찾을 수 없습니다."));
        quotationRequest.setProgress(QuotationProgress.USER_CANCELLED);
    }

    @Transactional
    public void cancelSellerQuotationRequest(UUID id) {
        QuotationRequest quotationRequest = quotationRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("견적 요청을 찾을 수 없습니다."));
        quotationRequest.setProgress(QuotationProgress.SELLER_CANCELLED);
    }

    @Transactional
    public void deleteQuotationRequest(UUID id) {
        quotationRequestRepository.deleteById(id);
    }


    // 현재 로그인한 멤버가 관리하는 회사 확인
    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인을 해주세요."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일에 매칭되는 사용자 정보가 없습니다."));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException("관리중인 업체가 없습니다."));
        return company;
    }

    private QuotationRequestInfoDto convertToQuotationRequestTotalInfoDto(QuotationRequest quotationRequest) {
        // 각 QuotationRequest에 대한 Quotation 및 RequestSolution 데이터를 가져와서 DTO로 변환
        List<QuotationInfoDto> quotationInfoDtos = quotationService.getQuotationInfoDtoListFrom(quotationRequest);
        List<SolutionForQuotationRequestDto> solutionDtos = solutionService.getSolutionListFor(quotationRequest);

        return new QuotationRequestInfoDto(quotationRequest, quotationInfoDtos, solutionDtos);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 작성한 모든 견적요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestOfMember(Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인을 해주세요."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일에 매칭되는 사용자 정보가 없습니다."));
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.findAllByMember(member, pageable);
        return quotationRequests.map(QuotationRequestInfoDto::new);
    }

    // (견적요청서를 작성한 사용자 권한) [딸려있는 solution, quotation 정보 포함] 사용자가 작성한 모든 견적요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestTotalInfoOfMember(Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인을 해주세요."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일에 매칭되는 사용자 정보가 없습니다."));
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.findAllByMember(member, pageable);
        // QuotationRequestTotalInfoDto로 매핑
        return quotationRequests.map(this::convertToQuotationRequestTotalInfoDto);
    }

    // (견적요청서를 작성한 사용자 권한) 사용자가 작성한 견적요청서 중 특정한 진행상태(progress)만 선택 출력 출력 (progress 소팅)
    public Page<QuotationRequestInfoDto> getAllQuotationRequestOfMember(QuotationProgress progress, Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인을 해주세요."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일에 매칭되는 사용자 정보가 없습니다."));
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.findAllByMemberAndProgress(member, progress, pageable);
        return quotationRequests.map(QuotationRequestInfoDto::new);
    }

    // (견적요청서를 작성한 사용자 권한) [딸려있는 solution, quotation 정보 포함] 사용자가 작성한 견적요청서 중 특정한 진행상태(progress)만 선택 출력 출력 (progress 소팅)
    public Page<QuotationRequestInfoDto> getAllQuotationRequestTotalInfoDtoOfMember(QuotationProgress progress, Pageable pageable) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException("로그인을 해주세요."));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("이메일에 매칭되는 사용자 정보가 없습니다."));
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.findAllByMemberAndProgress(member, progress, pageable);
        // QuotationRequestTotalInfoDto로 매핑
        return quotationRequests.map(this::convertToQuotationRequestTotalInfoDto);
    }

    public QuotationRequest findById(UUID id) {
        return quotationRequestRepository.findById(id).orElse(null);
    }

    // seller

    // (seller) 회사 관리자가 받은 모든 견적 요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestTowardCompany(Pageable pageable) {
        Company company = getCompanyOfMember();
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.getAllQuotationRequestTowardCompany(company.getId(), pageable);
        return quotationRequests.map(QuotationRequestInfoDto::new);
    }

    // (seller) 회사 관리자가 받은 특정 진행상태(progress)의 견적 요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestTowardCompany(QuotationProgress progress, Pageable pageable) {
        Company company = getCompanyOfMember();
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.getAllQuotationRequestTowardCompanySortedByProgress(company.getId(), progress, pageable);
        return quotationRequests.map(QuotationRequestInfoDto::new);
    }


    // (seller) 회사로부터 온 신청서 개수 Count
    public QuotationRequestCountDto getQuotationRequestCount() {
        Company company = getCompanyOfMember();
        List<Object[]> results = quotationRequestRepository.findQuotationRequestCountById(company.getId());
        return new QuotationRequestCountDto(results);
    }

    // admin

    // (admin) 특정 회사가 받은 모든 견적 요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestOfCompanyByAdmin(UUID companyId, Pageable pageable) {
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.getAllQuotationRequestTowardCompanyByAdmin(companyId, pageable);
        return quotationRequests.map(QuotationRequestInfoDto::new);
    }

    // (admin) 특정 사용자가 작성한 모든 견적 요청서 출력
    public Page<QuotationRequestInfoDto> getAllQuotationRequestOfMemberByAdmin(Member member, Pageable pageable) {
        Page<QuotationRequest> quotationRequests = quotationRequestRepository.findAllByMember(member, pageable);
        Page<QuotationRequestInfoDto> quotationRequestInfoDtos = quotationRequests.map(QuotationRequestInfoDto::new);
        return quotationRequestInfoDtos;
    }


    public Page<QuotationAdminRequestDto> findAllQutationRequestDto(Pageable pageable) {
        return quotationRequestRepository.findAll(pageable).map(QuotationAdminRequestDto::new);
    }

    public Page<QuotationAdminRequestDto> findAllByFilter(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "id" -> {
                    return quotationRequestRepository.findAllByIdContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "username" -> {
                    return quotationRequestRepository.findAllByUsernameContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "portfolioId" -> {
                    return quotationRequestRepository.findAllByPortfolioIdContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "title" -> {
                    return quotationRequestRepository.findAllByTitleContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "description" -> {
                    return quotationRequestRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "progress" -> {
                    return quotationRequestRepository.findAllByProgressContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "createdAt" -> {
                    return quotationRequestRepository.findAllByCreatedAtContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
                case "updatedAt" -> {
                    return quotationRequestRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(QuotationAdminRequestDto::new);
                }
            }
        } else {
            return quotationRequestRepository.findAll(pageable).map(QuotationAdminRequestDto::new);
        }
        throw new RuntimeException("findAllByFilter : QuotationService");
    }

    @Transactional
    public void updateProgressByIds(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        quotationRequestRepository.updateProgressByIds(ids);
    }

    @Transactional
    public void editQuotationRequestForAdmin(EditQuotationRequestDto editQuotationRequestDto) {
        Optional<QuotationRequest> quotationRequestOptional = quotationRequestRepository.findById(editQuotationRequestDto.getId());
        if (quotationRequestOptional.isPresent()) {
            QuotationRequest quotationRequest = quotationRequestOptional.get();
            quotationRequest.setProgress(editQuotationRequestDto.getProgress());
        } else {
            throw new NoSuchElementException("editQuotationRequestForAdmin");
        }
    }

    private List<QuotationProgress> stringToProgress(String progressStr) {

        if (progressStr.equals(QuotationProgress.PENDING.getProgress())) {
            return List.of(QuotationProgress.PENDING);
        } else if (progressStr.equals(QuotationProgress.APPROVED.getProgress())) {
            return List.of(QuotationProgress.APPROVED);
        } else {
            return List.of(QuotationProgress.PENDING, QuotationProgress.APPROVED, QuotationProgress.USER_CANCELLED, QuotationProgress.ADMIN_CANCELLED, QuotationProgress.SELLER_CANCELLED);
        }
    }
}
