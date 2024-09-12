package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyPortfolioDetailDto {

    private UUID companyId;
    private String companyName;
    private String description;
    private String phone;
    private String address;
    private String detailAddress;
    private boolean isApplied;
    private boolean isDeleted;
    private String url;

    public CompanyPortfolioDetailDto(Company company, String url) {
        this.companyId = company.getId();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.phone = company.getPhone();
        this.address = company.getAddress();
        this.detailAddress = company.getDetailAddress();
        this.isApplied = company.getIsApplied();
        this.isDeleted = company.isDeleted();
        this.url = url;
    }
}
