package com.user.IntArea.dto.portfolio;

import com.user.IntArea.entity.Solution;
import com.user.IntArea.entity.enums.Platform;
import lombok.Data;

import java.util.List;

@Data
public class PortfolioCreateDto {

    private String title;
    private String description;
}
