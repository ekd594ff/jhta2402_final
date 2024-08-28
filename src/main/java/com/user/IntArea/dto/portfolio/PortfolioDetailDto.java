package com.user.IntArea.dto.portfolio;

import com.user.IntArea.dto.company.CompanyPortfolioDetailDto;
import com.user.IntArea.dto.solution.SolutionPortfolioDto;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Solution;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDetailDto {

    private UUID portfolioId;
    private String title;
    private String description;
    private CompanyPortfolioDetailDto company;
    private List<String> imageUrl;
    private List<SolutionPortfolioDto> solution;

    @Builder
    public PortfolioDetailDto(Portfolio portfolio, String companyUrl, List<String> imageUrl, List<Solution> solution) {
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.company = new CompanyPortfolioDetailDto(portfolio.getCompany(), companyUrl);
        this.imageUrl = imageUrl;
        this.solution = solution.stream().map(SolutionPortfolioDto::new).toList();
    }
}
