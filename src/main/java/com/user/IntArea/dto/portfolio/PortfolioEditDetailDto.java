package com.user.IntArea.dto.portfolio;

import com.user.IntArea.dto.image.ImagePortfolioEditDto;
import com.user.IntArea.dto.solution.SolutionPortfolioDto;
import com.user.IntArea.entity.Image;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Solution;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioEditDetailDto {

    private UUID portfolioId;
    private String title;
    private String description;
    private List<ImagePortfolioEditDto> images;
    private List<SolutionPortfolioDto> solution;

    @Builder
    public PortfolioEditDetailDto(Portfolio portfolio, List<Image> images, List<Solution> solution) {
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.images = images.stream().map(ImagePortfolioEditDto::new).toList();
        this.solution = solution.stream().map(SolutionPortfolioDto::new).toList();
    }
}
