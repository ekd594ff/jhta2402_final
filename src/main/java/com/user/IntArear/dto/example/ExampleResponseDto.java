package com.user.IntArear.dto.example;

import com.user.IntArear.entity.Example;
import lombok.Data;

import java.util.UUID;

@Data
public class ExampleResponseDto {

    private UUID id;
    private String memberEmail;
    private String name;
    private String description;

    public ExampleResponseDto(Example example) {
        this.id = example.getId();
        this.memberEmail = example.getMember().getEmail();
        this.name = example.getName();
        this.description = example.getDescription();
    }
}
