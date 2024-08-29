package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.portfolio.PortfolioCreateDto;
import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.portfolio.PortfolioUpdateDto;
import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.repository.CompanyRepository;
import com.user.IntArea.repository.MemberRepository;
import com.user.IntArea.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    
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

    // (일반 권한) 특정한 하나의 포트폴리오 InfoDto 불러오기
    public PortfolioInfoDto getOpenPortfolioInfoById(UUID id) throws NoSuchElementException {
        try{
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
        } catch(Exception e) {
            throw new NoSuchElementException("");
        }
    }

    // seller 권한

    // (seller 권한) 포트폴리오 생성
    public Portfolio create(PortfolioCreateDto portfolioCreateDto) {
        Company company = getCompanyOfMember();
        Portfolio portfolio = Portfolio.builder()
                .title(portfolioCreateDto.getTitle())
                .description(portfolioCreateDto.getDescription())
                .company(company)
                .build();
        return portfolioRepository.save(portfolio);
    }

    // (seller 권한) 포트폴리오 수정
    public Portfolio updatePortfolio(PortfolioUpdateDto portfolioUpdateDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException(""));
        isCompanyManager(portfolio);
        portfolio.setTitle(portfolioUpdateDto.getTitle());
        portfolio.setDescription(portfolioUpdateDto.getDescription());
        return portfolioRepository.save(portfolio);
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
