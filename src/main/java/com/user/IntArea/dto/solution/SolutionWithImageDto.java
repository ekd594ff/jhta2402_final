package com.user.IntArea.dto.solution;

import jakarta.persistence.Tuple;
import lombok.Data;

import java.util.UUID;

@Data
public class SolutionWithImageDto {
    private UUID id;
    private String title;
    private Integer price;
    private String description;
    private UUID portfolioId;
    private String url;

    public SolutionWithImageDto(Tuple tuple) {
        this.id = (UUID) tuple.get("id");
        this.title = (String) tuple.get("title");
        this.price = (Integer) tuple.get("price");
        this.description = (String) tuple.get("description");
        this.portfolioId = (UUID) tuple.get("portfolioId");
        this.url = (String) tuple.get("url");
    }
}
