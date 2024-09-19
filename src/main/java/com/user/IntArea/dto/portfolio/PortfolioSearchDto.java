package com.user.IntArea.dto.portfolio;

import jakarta.persistence.Tuple;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
public class PortfolioSearchDto {
    private UUID id;
    private String title;
    private String companyName;
    private String description;
    private String imageUrlStr;
    private String[] imageUrls;
    private Double rate;

    public PortfolioSearchDto(UUID id, String title, String companyName, String description, String[] imageUrls, Double rate) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.description = description;
        this.imageUrls = imageUrls;
        this.rate = rate;
    }

    public PortfolioSearchDto(Tuple tuple) {
        this.id = (UUID) tuple.get("id");
        this.title = tuple.get("title").toString();
        this.companyName = tuple.get("companyName").toString();
        this.description = tuple.get("description").toString();
        this.imageUrls = (String[]) tuple.get("imageUrls");
        this.rate = tuple.get("rate", BigDecimal.class).doubleValue();
    }
}
