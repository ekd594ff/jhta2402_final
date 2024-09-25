package com.user.IntArea.dto.portfolio;

import lombok.Data;

import java.util.UUID;

@Data
public class EditPortfolioDto {
    private UUID id;
    private String title;
    private String description;
    private boolean deleted;
    private boolean activated;
}
