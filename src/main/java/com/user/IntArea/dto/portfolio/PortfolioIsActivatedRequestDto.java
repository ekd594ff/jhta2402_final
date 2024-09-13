package com.user.IntArea.dto.portfolio;

import lombok.Data;

import java.util.UUID;

@Data
public class PortfolioIsActivatedRequestDto {

    private UUID portfolioId;
    private boolean isActivated;
}
