package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class UnAppliedCompanyDto {

    private String memberUsername;
    private String memberEmail;
    private String companyName;
    private String description;
    private String phone;
    private String address;
    private String imageUrl;

    public UnAppliedCompanyDto(Member member, Company company, String url) {
        this.memberUsername = member.getUsername();
        this.memberEmail = member.getEmail();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.phone = company.getPhone();
        this.address = company.getAddress();
        this.imageUrl = url;
    }

    public UnAppliedCompanyDto(Member member, Company company) {
        this.memberUsername = member.getUsername();
        this.memberEmail = member.getEmail();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.phone = company.getPhone();
        this.address = company.getAddress();
    }
}
