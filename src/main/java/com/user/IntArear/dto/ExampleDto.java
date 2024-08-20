package com.user.IntArear.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor // 모든 변수가 들어있는 생성자
@Builder // builder 패턴으로 원하는 값만 넣어서 생성
public class ExampleDto {

    private UUID id;
    private UUID exampleMemberId;
    private String name;
    private String description;

}