package com.user.IntArea.dto.solution;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SolutionDto {
    private final String title;
    private final String description;
    private final int price;
    private final UUID id;
    private LocalDateTime createdAt;
}
