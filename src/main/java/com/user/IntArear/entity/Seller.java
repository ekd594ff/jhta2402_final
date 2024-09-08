package com.user.IntArear.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.File;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {
    @Id
    private Long id;
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Member representativeName; // 대표자명

    @NotNull
    private String businessRegistrationPath; //사업자 등록증 경로

    @NotNull
    private String representativeIDcardPath; //대표자 신분증경로

    @NotNull
    private Integer businessRegistrationNumber; //사업자 번호

    @NotNull
    private String address;

    private String detailAddress;

    @NotNull
    @Pattern(regexp = "^\\d{5}$") //5자리 숫자
    private int epost;

    @NotNull
    private String businessName; //상호명

    private String companyDescription; //한줄 소개

    @OneToMany(mappedBy = "seller")
    private List<Board> boards;
}

