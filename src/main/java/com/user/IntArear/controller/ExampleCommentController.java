package com.user.IntArear.controller;

import com.user.IntArear.dto.ExampleCommentDto;
import com.user.IntArear.dto.ExampleCommentResponseDto;
import com.user.IntArear.entity.ExampleComment;
import com.user.IntArear.service.ExampleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleCommentController {

    private final ExampleCommentService exampleCommentService;

    @GetMapping("/{id}/comment")
    public List<ExampleCommentResponseDto> getAll(@PathVariable(name = "id") UUID exampleId) {
        return exampleCommentService.findByExampleId(exampleId);
    }

    @PostMapping("/{id}/comment")
    public ExampleCommentResponseDto create(@PathVariable(name = "id") UUID exampleId,
                                 @RequestBody ExampleCommentDto exampleCommentDto) {

        return exampleCommentService.save(exampleId, exampleCommentDto);
    }
}
