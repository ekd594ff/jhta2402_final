package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Portfolio;
import lombok.Data;

import java.util.UUID;

@Data
public class PortfolioQuotationRequestDto {

    private UUID id;
    private String title;
    private String description;
    private String url;

    public PortfolioQuotationRequestDto(Portfolio portfolio, String url) {
        this.id = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.url = url;
    }
}
