package com.user.IntArear.dto.example;

import com.user.IntArear.entity.ExampleComment;
import lombok.Data;

import java.util.UUID;

@Data
public class ExampleCommentResponseDto {

    private UUID id;
    private String memberEmail;
    private String description;

    public ExampleCommentResponseDto(ExampleComment exampleComment) {
        this.id = exampleComment.getId();
        this.memberEmail = exampleComment.getMember().getEmail();
        this.description = exampleComment.getDescription();
    }
}
