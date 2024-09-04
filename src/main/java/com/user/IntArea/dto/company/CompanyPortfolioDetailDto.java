package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import lombok.Data;

@Data
public class CompanyPortfolioDetailDto {

    private String companyName;
    private String description;
    private String phone;
    private String address;
    private String url;

    public CompanyPortfolioDetailDto(Company company, String url) {
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.phone = company.getPhone();
        this.address = company.getAddress();
        this.url = url;
    }
}
