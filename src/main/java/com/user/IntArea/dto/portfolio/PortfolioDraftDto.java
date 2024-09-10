package com.user.IntArea.dto.portfolio;

import com.user.IntArea.dto.company.CompanyPortfolioDetailDto;
import com.user.IntArea.dto.solution.SolutionPortfolioDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDraftDto {

    private UUID portfolioId;
    private String title;
    private String description;
    private CompanyPortfolioDetailDto company;
    private List<String> imageUrl;
    private List<SolutionPortfolioDto> solution;

    public PortfolioDraftDto(UUID portfolioId, String title, String description, CompanyPortfolioDetailDto company, List<String> imageUrl, List<SolutionPortfolioDto> solution) {
        this.portfolioId = portfolioId;
        this.title = title;
        this.description = description;
        this.company = company;
        this.imageUrl = imageUrl;
        this.solution = solution;
    }
}
