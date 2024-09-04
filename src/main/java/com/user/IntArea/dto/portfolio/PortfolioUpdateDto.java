package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Solution;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PortfolioUpdateDto {

    private UUID id;
    private String title;
    private String description;
    private List<Solution> solution;
    private List<String> imageUrl;
    private LocalDateTime updatedAt;
}
