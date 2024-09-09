package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
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
    private String detailAddress;
    private boolean isApplied;
    private LocalDateTime createdAt;

    public CompanyResponseDto(Company company) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.phone = company.getPhone();
        this.address = company.getAddress();
        this.detailAddress = company.getDetailAddress();
        this.isApplied = company.getIsApplied();
        this.createdAt = company.getCreatedAt();
    }
}
