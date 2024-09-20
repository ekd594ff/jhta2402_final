package com.user.IntArea.service;

import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.portfolio.*;
import com.user.IntArea.dto.quotation.QuotationInfoDto;
import com.user.IntArea.dto.quotationRequest.QuotationRequestDto;
import com.user.IntArea.dto.solution.SolutionDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final CustomPortfolioRepositoryImpl customPortfolioRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ImageUtil imageUtil;
    private final ImageRepository imageRepository;
    private final SolutionRepository solutionRepository;
    private final ImageService imageService;
    private final QuotationRepository quotationRepository;
    private final QuotationRequestRepository quotationRequestRepository;

    // 포트폴리오에 접근하는 멤버가 그 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void isCompanyManager(Portfolio portfolio) {
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

    // Quotation을 QuotationInfoDto로 변환하는 메서드
    private QuotationInfoDto convertToQuotationInfoDto(Quotation quotation) {
        List<String> imageUrls = new ArrayList<>();
        try { // 견적서와 관련된 이미지 로드
            List<Image> imageDtos = imageService.getImagesFrom(quotation);
            if (imageDtos != null) {
                imageUrls = imageDtos.stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) { // 예외 발생 시 로그 남기기
            System.err.println("이미지 로드 실패: " + e.getMessage());
        }
        // QuotationInfoDto로 매핑
        return new QuotationInfoDto(quotation, imageUrls);
    }

    // QuotationRequest를 dto로 변환하는 매서드
    private QuotationRequestDto convertToQuotationRequestDto(QuotationRequest quotationRequest) {
        // Mapping RequestSolution to SolutionDto
        List<SolutionDto> solutionDtos = quotationRequest.getRequestSolutions().stream()
                .map(requestSolution -> new SolutionDto(requestSolution.getSolution())) // Assuming SolutionDto has a constructor that takes a Solution entity
                .collect(Collectors.toList());

        return QuotationRequestDto.builder()
                .memberId(quotationRequest.getMember().getId())
                .portfolioId(quotationRequest.getPortfolio().getId())
                .title(quotationRequest.getTitle())
                .description(quotationRequest.getDescription())
                .progress(quotationRequest.getProgress().toString())
                .solutions(solutionDtos)
                .build();
    }

    private PortfolioAllInfoDto getPortfolioAllInfoDtoFrom(Portfolio portfolio) {

        // Portfolio 내의 Solutions와 QuotationRequests가 초기화되지 않았을 경우 로드 (Entity에서 FetchType이 lazy로 설정되어있기 때문에 필요함)
        if (portfolio.getSolutions().isEmpty()) {
            portfolio.setSolutions(solutionRepository.findByPortfolioId(portfolio.getId()));
        }
        if (portfolio.getQuotationRequests().isEmpty()) {
            portfolio.setQuotationRequests(quotationRequestRepository.findByPortfolioId(portfolio.getId()));
        }

        // 견적 정보를 DTO로 변환
        List<Quotation> quotations = quotationRepository.getAllByPortfolioId(portfolio.getId());
        List<QuotationInfoDto> quotationInfoDtos = quotations.stream()
                .map(this::convertToQuotationInfoDto)
                .collect(Collectors.toList());

        // 이미지 URL 리스트 생성
        List<Image> images = imageService.getImagesFrom(portfolio);
        List<String> imageUrls = new ArrayList<>();
        ;
        for (Image image : images) {
            imageUrls.add(image.getUrl());
        }

        // Solution을 dto로 변환
        List<SolutionDto> solutionDtos = portfolio.getSolutions().stream()
                .map(SolutionDto::new)  // 엔티티를 DTO로 변환
                .collect(Collectors.toList());

        // QuotationRequest을 dto로 변환
        List<QuotationRequestDto> quotationRequestDtos = portfolio.getQuotationRequests().stream()
                .map(QuotationRequestDto::new)
                .collect(Collectors.toList());

        return PortfolioAllInfoDto.builder()
                .portfolio(portfolio)
                .quotations(quotationInfoDtos)
                .imageUrls(imageUrls)
                .quotationRequests(quotationRequestDtos)
                .solutions(solutionDtos)
                .build();
    }

    // (일반 권한) DB에 존재하는 공개된 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtos(Pageable pageable) {
        Page<Portfolio> portfolios = portfolioRepository.getOpenPortfolios(pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (일반 권한) 특정 단어로 검색한 공개된 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtosWithSearchWord(String searchWord, Pageable pageable) {
        Page<Portfolio> portfolios = portfolioRepository.getOpenPortfoliosWithSearchWord(searchWord, pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (일반 권한) 특정 회사의 오픈된 포트폴리오 InfoDto 불러오기
    public Page<PortfolioDetailInfoDto> getOpenPortfolioInfoDtosOfCompany(UUID companyId, Pageable pageable) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("해당 회사가 없습니다."));
        Page<Portfolio> portfolios = portfolioRepository.findAllByCompanyAndIsDeletedAndIsActivated(company, false, true, pageable);

        return portfolios.map((portfolio) -> {
            List<String> imageUrls = imageRepository.findAllByRefId(portfolio.getId()).stream().map(Image::getUrl).toList();

            return new PortfolioDetailInfoDto(portfolio, imageUrls);
        });
    }

    // (일반 권한) 검색된 포트폴리오 검색 메서드
    public Page<PortfolioSearchDto> findPortfolioBySearchWord(String searchWord, String sortField, String sortDirection, Pageable pageable) {
        if (sortField.equals("createdAt")) {
            sortField = "p.createdAt";
        }
        if (sortField.equals("companyName")) {
            sortField = "c.companyName";
        }
        return customPortfolioRepository.findPortfolioBySearchWord(searchWord, sortField, sortDirection,pageable)
                .map(PortfolioSearchDto::new);
    }

//    public List<PortfolioRecommendDto> getTop8RecommendedPortfolioByAvgRate() {
//        return portfolioRepository.getRecommendedPortfolioByAvgRate()
//    }

    // (일반 권한) 특정한 하나의 포트폴리오 DetailInfoDto 불러오기
    public PortfolioDetailInfoDto getOpenPortfolioInfoById(UUID id) {

        Portfolio portfolio = portfolioRepository.findByIdAndIsDeletedAndIsActivated(id, false, true)
                .orElseThrow(() -> new NoSuchElementException(""));

        List<String> portfolioUrls = imageRepository.findAllByRefId(portfolio.getId())
                .stream().map(Image::getUrl)
                .toList();

        return new PortfolioDetailInfoDto(portfolio, portfolioUrls);
    }

    // (일반 권한) 특정한 갯수만큼 램덤한 포트폴리오 InfoDto 불러오기
    public List<Map<String, Object>> getRandomPortfolioInfoDtos(int count) {
        List<Map<String, Object>> portfolios = portfolioRepository.getRandomPortfolioInfoDtos(count);
        return portfolios;
    }

    // (일반 권한) 특정한 갯수만큼 램덤한 PortfolioAllInfoDto 불러오기(quotation, 이미지 url 등 포함)
    public List<PortfolioAllInfoDto> getRandomPortfolioAllInfoDtos(int count) {
        List<Portfolio> selectedPortfolios = portfolioRepository.getRandomPortfolio(count);

        // Portfolio를 DTO로 변환
        List<PortfolioAllInfoDto> portfolioAllInfoDtos = new ArrayList<>();
        for (Portfolio portfolio : selectedPortfolios) {
            PortfolioAllInfoDto portfolioInfoDto = getPortfolioAllInfoDtoFrom(portfolio);
            portfolioAllInfoDtos.add(portfolioInfoDto);
        }
        return portfolioAllInfoDtos;
    }

    // (일반 권한) 1순위부터 count 순위까지의 PortfolioRateDtos 불러오기
    public List<Map<String, Object>> getRecommendedPortfolioByAvgRate(Pageable pageable) {
        return portfolioRepository.getRecommendedPortfolioByAvgRate(pageable);
    }

    // seller 권한

    // (seller 권한) 포트폴리오 생성
    @Transactional
    public Portfolio create(PortfolioRequestDto portfolioRequestDto) {

        Company company = getCompanyOfMember();

        Portfolio portfolio = Portfolio.builder()
                .title(portfolioRequestDto.getTitle())
                .description(portfolioRequestDto.getDescription())
                .company(company)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        //  이미지 저장
        if (portfolioRequestDto.getImages() != null) {
            for (int i = 0; i < portfolioRequestDto.getImages().size(); i++) {

                MultipartFile image = portfolioRequestDto.getImages().get(i);

                ImageDto imageDto = imageUtil.uploadS3(
                                image,
                                savedPortfolio.getId(),
                                i)
                        .orElseThrow(() -> new NoSuchElementException("이미지 저장에 문제가 발생했습니다."));

                imageRepository.save(imageDto.toImage());
            }
        }

        // 솔루션 저장
        List<Solution> solutions = portfolioRequestDto.getSolutions().stream()
                .map(solutionDto -> solutionDto.toSolution(portfolio))
                .toList();
        solutionRepository.saveAll(solutions);

        return savedPortfolio;
    }

    // (seller 권한) 포트폴리오 id로 검색, 본인 것이 아닌 경우 Exception
    public PortfolioEditDetailDto getMyPortfolioInfoById(UUID id) {

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 포트폴리오가 없습니다."));

        isCompanyManager(portfolio);

        List<Image> images = imageRepository.findAllByRefId(portfolio.getId());

        List<Solution> solutions = solutionRepository.findAllByPortfolioId(portfolio.getId());

        return PortfolioEditDetailDto.builder()
                .portfolio(portfolio)
                .images(images)
                .solution(solutions)
                .build();
    }

    // (seller 권한) 포트폴리오 수정
    @Transactional
    public void updatePortfolio(PortfolioRequestDto portfolioRequestDto) {

        // 포트폴리오 수정 권한 확인
        Portfolio portfolio = portfolioRepository.findById(portfolioRequestDto.getId())
                .orElseThrow(() -> new NoSuchElementException(""));
        isCompanyManager(portfolio);

        // 포트폴리오 수정
        portfolio.setTitle(portfolioRequestDto.getTitle());
        portfolio.setDescription(portfolioRequestDto.getDescription());
        portfolioRepository.save(portfolio);

        // 삭제된 이미지 제거
        List<MultipartFile> images = portfolioRequestDto.getImages();
        List<UUID> imageIdList = images.stream()
                .filter(image -> {
                    try {
                        UUID.fromString(Objects.requireNonNull(image.getOriginalFilename()));
                        return true; // 변환 가능
                    } catch (IllegalArgumentException e) {
                        return false; // 변환 불가능
                    }
                })
                .map(image -> UUID.fromString(image.getOriginalFilename())) // 변환
                .toList(); // 리스트로 수집
        imageRepository.deleteAllNotInId(imageIdList);

        // 이미지 저장 및 수정
        for (int i = 0; i < images.size(); i++) {

            MultipartFile image = images.get(i);
            if (image.isEmpty()) {
                // 기존 이미지 테이블 id로 S3 이름 변경, 이미지 테이블 업데이트
                Image dbImage = imageRepository.findById(UUID.fromString(Objects.requireNonNull(image.getOriginalFilename())))
                        .orElseThrow(() -> new NoSuchElementException("해당 이미지가 없습니다."));
                ImageDto imageDto = imageUtil.renameS3(dbImage.getUrl(), portfolio.getId(), i)
                        .orElseThrow(() -> new NoSuchElementException("S3 오류"));

                dbImage.setUrl(imageDto.getUrl());
                dbImage.setFilename(imageDto.getFilename());
                imageRepository.save(dbImage);

            } else {
                ImageDto imageDto = imageUtil.uploadS3(image, portfolio.getId(), i)
                        .orElseThrow(() -> new NoSuchElementException("S3 오류"));
                imageRepository.save(imageDto.toImage());
            }
        }

        // 기존 솔루션 isDeleted = true, 새로운 solution 등록
        solutionRepository.updateIsDeletedByPortfolioId(portfolio.getId());

        List<Solution> solutions = portfolioRequestDto.getSolutions().stream()
                .map(solutionDto -> solutionDto.toSolution(portfolio))
                .toList();
        solutionRepository.saveAll(solutions);
    }

    // (seller 권한) 회사 관리자가 관리하는 회사의 모든 포트폴리오 InfoDto 불러오기
    public Page<PortfolioDetailInfoDto> getCompanyPortfolioInfoDtosByCompanyManager(Pageable pageable) throws NoSuchElementException {
        Company company = getCompanyOfMember();
        Page<Portfolio> portfolios = portfolioRepository.findAllByCompanyAndIsDeleted(company, false, pageable);

        return portfolios.map((portfolio) -> {
            List<String> imageUrls = imageRepository.findAllByRefId(portfolio.getId()).stream().map(Image::getUrl).toList();

            return new PortfolioDetailInfoDto(portfolio, imageUrls);
        });
    }

    // (seller 권한) 포트폴리오 삭제(회사 관리자만 과거의 작성기록으로서 열람 가능, 수정 불가)
    public void softDeletePortfolio(UUID id) {
        Company company = getCompanyOfMember();
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("포트폴리오가 없습니다."));

        if (!portfolio.getCompany().equals(company)) {
            throw new NoSuchElementException("본인 회사가 아닙니다.");
        }

        portfolio.setDeleted(true);
        portfolioRepository.save(portfolio);
    }

    // (seller 권한) 포트폴리오 활성화-비활성화(일반 고객에게 비공개-회사 관리자만 열람 및 수정 가능)
    public void updateActivatePortfolio(PortfolioIsActivatedRequestDto dto) {
        Company company = getCompanyOfMember();

        Portfolio portfolio = portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new NoSuchElementException("포트폴리오가 없습니다."));

        if (!portfolio.getCompany().equals(company)) {
            throw new NoSuchElementException("본인 소유 회사가 아닙니다.");
        }

        portfolio.setActivated(dto.isActivated());

        portfolioRepository.save(portfolio);
    }

    // admin 권한

    // (admin 권한) 특정한 하나의 포트폴리오 InfoDto 불러오기
    public PortfolioInfoDto getPortfolioInfoByIdByAdmin(UUID id) throws NoSuchElementException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
        PortfolioInfoDto portfolioInfoDto = PortfolioInfoDto.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .companyName(portfolio.getCompany().getCompanyName())
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .isDeleted(portfolio.isDeleted())
                .build();
        return portfolioInfoDto;
    }

    // (admin 권한) DB에 존재하는 모든 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosByAdmin(Pageable pageable) {
        Page<Portfolio> portfolios = portfolioRepository.findAll(pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (admin 권한) 회사 아이디를 이용하여 특정 회사의 모든 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosOfCompanyByAdmin(UUID companyId, Pageable pageable) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException(""));
        Page<Portfolio> portfolios = portfolioRepository.findAllByCompany(company, pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (admin 권한) 특정 단어로 검색한 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getAllPortfolioInfoDtosWithSearchWordByAdmin(String searchWord, Pageable pageable) {
        Page<Portfolio> portfolios = portfolioRepository.getAllPortfoliosWithSearchWordByAdmin(searchWord, pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (admin 권한) 포트폴리오 수정
    public Portfolio updatePortfolioByAdmin(PortfolioUpdateDto portfolioUpdateDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException(""));
        portfolio.setTitle(portfolioUpdateDto.getTitle());
        portfolio.setDescription(portfolioUpdateDto.getDescription());
        return portfolioRepository.save(portfolio);
    }

    // (admin 권한) 포트폴리오 DB에서 삭제
    public void deletePortfolioByAdmin(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));
        portfolioRepository.deleteById(id);
    }

    // (admin 권한) 포트폴리오 SOFT DELETE
    public void softDeletePortfolioByAdmin(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));
        isCompanyManager(portfolio);
        portfolio.setDeleted(true);
        portfolioRepository.save(portfolio);
    }

    // (admin 권한) 포트폴리오 활성화-비활성화 (todo : 엔티티 수정 이후 작성)
    public void activatePortfolioByAdmin(UUID id, boolean isActivated) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));
        isCompanyManager(portfolio);
//        portfolio.setActivated(!isActivated);
        portfolioRepository.save(portfolio);
    }

    // (admin 권한)  포트폴리오 리스트 출력
    public Page<PortfolioInfoDto> getSearchPortfolio(Optional<String> filterColumn, Optional<String> filterValue, Pageable pageable) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "title" -> {
                    return portfolioRepository.findAllByTitleContains(filterValue.get(), pageable).map(PortfolioInfoDto::new);
                }
                case "description" -> {
                    return portfolioRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(PortfolioInfoDto::new);
                }
                case "companyName" -> {
                    return portfolioRepository.findAllByCompanyNameContains(filterValue.get(), pageable).map(PortfolioInfoDto::new);
                }
                case "updatedAt" -> {
                    return portfolioRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(PortfolioInfoDto::new);
                }
                case "createdAt" -> {
                    return portfolioRepository.findAllByCreatedAtContains(filterColumn.get(), pageable).map(PortfolioInfoDto::new);
                }
                case "deleted" -> {
                    if (filterValue.get().equals("true")) {
                        return portfolioRepository.findAllByDeletedIs(true, pageable).map(PortfolioInfoDto::new);
                    } else {
                        return portfolioRepository.findAllByDeletedIs(false, pageable).map(PortfolioInfoDto::new);
                    }
                }
            }
        } else {
            return portfolioRepository.findAll(pageable).map(PortfolioInfoDto::new);
        }
        throw new RuntimeException("getSearchPortfolio");
    }

    public List<Map<String, Object>> getRecentTransactionPortfolioList(Integer count) {
        return portfolioRepository.getRecentTransactionPortfolioList(count);
    }

    @Transactional
    public void softDeletePortfolios(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        portfolioRepository.softDeleteByIds(ids);
    }

    @Transactional
    public void hardDeletePortfolios(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        portfolioRepository.deleteAllById(ids);
    }

    @Transactional
    public void editPortfolioForAdmin(EditPortfolioDto editPortfolioDto) {
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(editPortfolioDto.getId());
        if (portfolioOptional.isPresent()) {
            Portfolio portfolio = portfolioOptional.get();
            portfolio.setDescription(editPortfolioDto.getDescription());
            portfolio.setTitle(editPortfolioDto.getTitle());
            portfolio.setDeleted(editPortfolioDto.isDeleted());
            portfolio.setActivated(editPortfolioDto.isActivated());
        } else {
            throw new NoSuchElementException("editPortfolioForAdmin");
        }
    }
}