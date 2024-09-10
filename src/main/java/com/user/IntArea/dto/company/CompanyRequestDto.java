package com.user.IntArea.dto.company;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class CompanyRequestDto {

    private final String companyName;
    private final String description;
    private final String phone;
    private final String address;
    private final String detailAddress;
    private MultipartFile image;

    public Company toEntity(Member member) {
        return Company.builder()
                .member(member)
                .companyName(companyName)
                .description(description)
                .phone(phone)
                .address(address)
                .detailAddress(detailAddress)
                .isApplied(false)
                .build();
    }
}
