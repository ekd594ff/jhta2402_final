package com.user.IntArea.service;

import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.portfolio.*;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ImageUtil imageUtil;
    private final ImageRepository imageRepository;
    private final SolutionRepository solutionRepository;

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

    // 일반 권한

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
    public Page<PortfolioInfoDto> getOpenPortfolioInfoDtosOfCompany(UUID companyId, Pageable pageable) {
        Page<Portfolio> portfolios = portfolioRepository.getOpenPortfolioInfoDtosOfCompany(companyId, pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (일반 권한) 검색된 포트폴리오 검색 메서드
    public Page<PortfolioSearchDto> getPortfolios(String searchWord, Pageable pageable) {
        return portfolioRepository.searchPortfolios(searchWord, pageable)
                .map(result -> new PortfolioSearchDto((String) result[0], (String) result[1], (String) result[2], (String[]) result[3], result[4].toString()));
    }

    // (일반 권한) 특정한 하나의 포트폴리오 InfoDto 불러오기
    public PortfolioInfoDto getOpenPortfolioInfoById(UUID id) throws NoSuchElementException {
        try {
            Portfolio portfolio = portfolioRepository.getOpenPortfolioInfoById(id);
            PortfolioInfoDto portfolioInfoDto = PortfolioInfoDto.builder()
                    .title(portfolio.getTitle())
                    .description(portfolio.getDescription())
                    .companyName(portfolio.getCompany().getCompanyName())
                    .createdAt(portfolio.getCreatedAt())
                    .updatedAt(portfolio.getUpdatedAt())
                    .isDeleted(portfolio.isDeleted())
                    .build();
            return portfolioInfoDto;
        } catch (Exception e) {
            throw new NoSuchElementException("");
        }
    }

    // (일반 권한) 특정한 갯수만큼 램덤한 포트폴리오 InfoDto 불러오기
    public List<PortfolioInfoDto> getRandomPortfolioInfoDtos(int count) {
        List<Portfolio> portfolios = portfolioRepository.getRandomPortfolioInfoDtos(count);
        List<PortfolioInfoDto> portfolioInfoDtos = portfolios.stream().map(PortfolioInfoDto::new).collect(Collectors.toList());
        return portfolioInfoDtos;
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
    public Page<PortfolioInfoDto> getCompanyPortfolioInfoDtosByCompanyManager(Pageable pageable) throws NoSuchElementException {
        Company company = getCompanyOfMember();
        Page<Portfolio> portfolios = portfolioRepository.findAllByCompany(company, pageable);
        return portfolios.map(PortfolioInfoDto::new);
    }

    // (seller 권한) 포트폴리오 삭제(회사 관리자만 과거의 작성기록으로서 열람 가능, 수정 불가)
    public void softDeletePortfolio(UUID id) {
        Company company = getCompanyOfMember();
        Portfolio portfolio = portfolioRepository.findByIdByCompanyManager(id, company.getId());
        isCompanyManager(portfolio);
        portfolio.setDeleted(true);
        portfolioRepository.save(portfolio);
    }

    // (seller 권한) 포트폴리오 활성화-비활성화(일반 고객에게 비공개-회사 관리자만 열람 및 수정 가능) (todo : 엔티티 수정 이후 작성)
    public void activatePortfolio(UUID id, boolean isActivated) {
        Company company = getCompanyOfMember();
        Portfolio portfolio = portfolioRepository.findByIdByCompanyManager(id, company.getId());
        isCompanyManager(portfolio);
//        portfolio.setActivated(!isActivated);
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

}