package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import lombok.Data;

@Data
public class CompanyRequestDto {

    private final String companyName;
    private final String description;
    private final String phone;
    private final String address;

    public Company toEntity(Member member) {
        return Company.builder()
                .member(member)
                .companyManager(member.getUsername())
                .companyName(companyName)
                .description(description)
                .phone(phone)
                .address(address)
                .isApplied(false)
                .build();
    }
}
