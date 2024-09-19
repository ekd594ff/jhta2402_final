package com.user.IntArea.dto.portfolio;

import jakarta.persistence.Tuple;
import lombok.Data;

import java.util.UUID;

@Data
public class PortfolioRecommendDto {
    private UUID id;
    private String title;
    private Double rate;
    private String description;

    public PortfolioRecommendDto(Tuple tuple) {
        this.id = (UUID) tuple.get("id");
        this.title = (String) tuple.get("title");
        this.rate = (Double) tuple.get("rate");
        this.description = (String) tuple.get("description");
    }
}
