package com.user.IntArea.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CompanyResponseDto {

    private UUID id;
    private String companyName;
    private String description;
    private String phone;
    private String address;
    private Boolean isApplied;
    private LocalDateTime createdAt;
}
