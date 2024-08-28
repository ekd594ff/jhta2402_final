package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PortfolioInfoDto {

    private String title;
    private String description;
    private String companyName;
    private LocalDateTime createDate;
//    private List<Solution> solution;

    public PortfolioInfoDto(Portfolio portfolio) {
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.companyName = portfolio.getCompany().getCompanyName();
        this.createDate = portfolio.getCreatedAt();
    }

    @Builder
    public PortfolioInfoDto(String title, String description, String companyName, LocalDateTime createDate) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.createDate = createDate;
    }
}
