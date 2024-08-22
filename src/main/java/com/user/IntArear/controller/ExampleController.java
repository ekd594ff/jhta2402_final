package com.user.IntArear.controller;

import com.user.IntArear.dto.example.ExampleDto;
import com.user.IntArear.dto.example.ExampleResponseDto;
import com.user.IntArear.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    // CREATE: 새로운 Example 생성
    @PostMapping("/example")
    public ExampleResponseDto createExample(@RequestBody ExampleDto exampleDto) {

        return exampleService.saveExample(exampleDto);
    }

    // READ: 특정 ID로 Example 조회
    @GetMapping("/example/{id}")
    public ExampleResponseDto getExampleById(@PathVariable UUID id) {
        return exampleService.getExampleById(id);
    }

    // READ: 모든 Example 조회
    @GetMapping("/example/all")
    public List<ExampleResponseDto> getAllProducts() {
        return exampleService.getAllExamples();
    }

    // READ: 페이지 EXAMPLE 조회
    @GetMapping("/example")
    public Page<ExampleResponseDto> getExamples(@RequestParam int page, @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);

        return exampleService.getExamples(pageable);
    }

    // UPDATE: 특정 ID의 Example 수정
    @PutMapping("/example/{id}")
    public ExampleResponseDto updateExample(@PathVariable UUID id, @RequestBody ExampleDto exampleDto) {
        return exampleService.updateExample(id, exampleDto);
    }

    // DELETE: 특정 ID의 Example 삭제
    @DeleteMapping("/example/{id}")
    public void deleteExample(@PathVariable UUID id) {
        exampleService.deleteExample(id);
    }

    // DELETE: 특정 ID의 Example 삭제 (admin)
    @DeleteMapping("/admin/example/{id}")
    public void deleteExampleAdmin(@PathVariable UUID id) {
        exampleService.deleteExampleAdmin(id);
    }
}