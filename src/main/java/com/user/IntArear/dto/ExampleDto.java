package com.user.IntArear.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExampleDto {

    private Long id;
    private String name;
    private String description;

    // 생성자
    public ExampleDto() {}

    public ExampleDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
