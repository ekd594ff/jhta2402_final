package com.user.IntArea.dto.company;

import lombok.Data;

import java.util.UUID;

@Data
public class EditCompanyDto {

    private UUID id;
    private String companyName;
    private String description;
    private String phone;
    private String address;
    private String detailAddress;
    private boolean applied;
}
