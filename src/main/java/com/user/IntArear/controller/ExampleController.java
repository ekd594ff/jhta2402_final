package com.user.IntArear.controller;

import com.user.IntArear.dto.ExampleDto;
import com.user.IntArear.entity.Example;
import com.user.IntArear.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    // CREATE: 새로운 Example 생성
    @PostMapping
    public Example createExample(@RequestBody ExampleDto exampleDto) {
        return exampleService.saveExample(exampleDto);
    }

    // READ: 특정 ID로 Example 조회
    @GetMapping("/{id}")
    public Example getExampleById(@PathVariable UUID id) {
        return exampleService.getExampleById(id);
    }

    // READ: 모든 Example 조회
    @GetMapping
    public List<Example> getAllProducts() {
        return exampleService.getAllExamples();
    }

    // UPDATE: 특정 ID의 Example 수정
    @PutMapping("/{id}")
    public Example updateExample(@PathVariable UUID id, @RequestBody ExampleDto exampleDto) {
        return exampleService.updateExample(id, exampleDto);
    }

    // DELETE: 특정 ID의 Example 삭제
    @DeleteMapping("/{id}")
    public void deleteExample(@PathVariable UUID id) {
        exampleService.deleteExample(id);
    }

}