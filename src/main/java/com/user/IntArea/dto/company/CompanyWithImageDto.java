package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Image;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyWithImageDto {
    private UUID id;
    private String companyName;
    private String description;
    private String url;

    public CompanyWithImageDto(Company company, Image image) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.url = image.getUrl();
    }

    public CompanyWithImageDto(Company company) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
    }
}
