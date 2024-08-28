package com.user.IntArea.service;

import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.portfolio.PortfolioCreateDto;
import com.user.IntArea.dto.portfolio.PortfolioDetailDto;
import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.dto.portfolio.PortfolioUpdateDto;
import com.user.IntArea.entity.*;
import com.user.IntArea.repository.*;
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
    private final SolutionRepository solutionRepository;
    private final ImageRepository imageRepository;


    // 포트폴리오에 접근하는 멤버가 그 포트폴리오를 제작한 회사의 관리자가 맞는지 확인
    private void isCompanyManager(Portfolio portfolio) {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));

        if (!company.equals(portfolio.getCompany())) {
            throw new NoSuchElementException("");
        }
        ;
    }

    public PortfolioDetailDto getPortfolioById(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));
        List<Solution> solutionDtos = solutionRepository.findAllByPortfolioId(portfolio.getId());
        List<String> portfolioUrls = imageRepository.findAllByRefId(portfolio.getId())
                .stream().map(Image::getUrl)
                .toList();

        String companyImageUrl = null;
        Optional<Image> optionalImage = imageRepository.findByRefId(portfolio.getCompany().getId());
        if (optionalImage.isPresent()) {
            companyImageUrl = optionalImage.get().getUrl();
        }

        return PortfolioDetailDto.builder()
                .portfolio(portfolio)
                .imageUrl(portfolioUrls)
                .companyUrl(companyImageUrl)
                .solution(solutionDtos)
                .build();
    }


    // (회사 관리자 권한) 포트폴리오 생성
    public Portfolio create(PortfolioCreateDto portfolioCreateDto) {

        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));

        Portfolio portfolio = Portfolio.builder()
                .title(portfolioCreateDto.getTitle())
                .description(portfolioCreateDto.getDescription())
                .company(company)
                .build();
        return portfolioRepository.save(portfolio);
    }

    // (회사 관리자 권한) 포트폴리오 수정
    public Portfolio updatePortfolio(PortfolioUpdateDto portfolioUpdateDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException(""));

        isCompanyManager(portfolio);

        portfolio.setTitle(portfolioUpdateDto.getTitle());
        portfolio.setDescription(portfolioUpdateDto.getDescription());
        return portfolioRepository.save(portfolio);
    }

    // (Admin 권한) 포트폴리오 수정
    public Portfolio updatePortfolioByAdmin(PortfolioUpdateDto portfolioUpdateDto) {
        Portfolio portfolio = portfolioRepository.findById(portfolioUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException(""));
        portfolio.setTitle(portfolioUpdateDto.getTitle());
        portfolio.setDescription(portfolioUpdateDto.getDescription());
        return portfolioRepository.save(portfolio);
    }

    // 특정한 하나의 포트폴리오 InfoDto 불러오기
    public PortfolioInfoDto getPortfolioInfoById(UUID id) throws NoSuchElementException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
        PortfolioInfoDto portfolioInfoDto = PortfolioInfoDto.builder()
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .companyName(portfolio.getCompany().getCompanyName())
                .createDate(portfolio.getCreatedAt())
                .build();
        return portfolioInfoDto;
    }

    // (회사 관리자 권한) 회사 관리자가 관리하는 회사의 모든 포트폴리오 InfoDto 불러오기
    public List<PortfolioInfoDto> getCompanyPortfoliosByCompanyManager() throws NoSuchElementException {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));

        List<PortfolioInfoDto> companyPortfolioInfoDtos = new ArrayList<>();
        for (Portfolio portfolio : portfolioRepository.findAllByCompany(company)) {
            companyPortfolioInfoDtos.add(new PortfolioInfoDto(portfolio));
        }

        return companyPortfolioInfoDtos;
    }

    // (admin 권한) 회사 아이디를 이용하여 특정 회사의 모든 포트폴리오 InfoDto 불러오기
    public List<PortfolioInfoDto> getCompanyPortfolioInfoDtos(UUID companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException(""));
        List<Portfolio> portfolioList = portfolioRepository.findAllByCompany(company);

        List<PortfolioInfoDto> companyPortfolioInfoDtos = new ArrayList<>();
        for (Portfolio portfolio : portfolioList) {
            companyPortfolioInfoDtos.add(new PortfolioInfoDto(portfolio));
        }
        return companyPortfolioInfoDtos;
    }

    // DB에 존재하는 모든 포트폴리오 InfoDto 불러오기
    public List<PortfolioInfoDto> getAllPortfolioInfoDtos() {
        List<PortfolioInfoDto> allPortfolioInfoDtos = new ArrayList<>();
        for (Portfolio portfolio : portfolioRepository.findAll()) {
            allPortfolioInfoDtos.add(new PortfolioInfoDto(portfolio));
        }
        return allPortfolioInfoDtos;
    }

    public List<PortfolioInfoDto> test(String searchWord) {
        return portfolioRepository.test(searchWord)
                .stream().map(portfolio -> new PortfolioInfoDto(portfolio))
                .toList();
    }

    // 특정 단어로 검색한 포트폴리오 InfoDto 불러오기
    public Page<PortfolioInfoDto> getPortfolioInfoDtosWithSearchWord(String searchWord, Pageable pageable) {

//        Page<PortfolioInfoDto> portfolioInfoDtos = portfolioRepository.getPortfoliosWithSearchWord(searchWord, pageable)
//                .map(PortfolioInfoDto::new);
//        return portfolioInfoDtos;

        Page<Portfolio> portfolios = portfolioRepository.getPortfoliosWithSearchWord(searchWord, pageable);
        return portfolios.map(portfolio -> new PortfolioInfoDto(portfolio));
    }

    // 포트폴리오 DB에서 삭제(todo : 엔티티 수정 이후 작성)
    public void deletePortfolio(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));

        isCompanyManager(portfolio);

        portfolioRepository.deleteById(id);
    }

    // 포트폴리오 삭제(회사 관리자만 과거의 작성기록으로서 열람 가능, 수정 불가) (todo : 엔티티 수정 이후 작성)
    public void softDeletePortfolio(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));

        isCompanyManager(portfolio);

//        portfolio.setDescription("");
//        portfolioRepository.save(portfolio);
    }

    // 포트폴리오 활성화-비활성화(일반 고객에게 비공개-회사 관리자만 열람 및 수정 가능) (todo : 엔티티 수정 이후 작성)
    public void activatePortfolio(UUID id, boolean activate) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));

        isCompanyManager(portfolio);

//        portfolio.setDescription("true");
//        portfolioRepository.save(portfolio);
    }

}
