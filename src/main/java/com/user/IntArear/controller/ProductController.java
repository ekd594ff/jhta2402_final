package com.user.IntArear.controller;

import com.user.IntArear.entity.Example;
import com.user.IntArear.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ExampleService exampleService;

    // CREATE: 새로운 Example 생성
    @PostMapping
    public Example createExample(@RequestBody Example example) {
        return exampleService.saveExample(example);
    }

    // READ: 특정 ID로 Example 조회
    @GetMapping("/{id}")
    public Example getExampleById(@PathVariable Long id) {
        return exampleService.getExampleById(id);
    }

    // READ: 모든 Example 조회
    @GetMapping
    public List<Example> getAllProducts() {
        return exampleService.getAllExamples();
    }

    // UPDATE: 특정 ID의 Example 수정
    @PutMapping("/{id}")
    public Example updateExample(@PathVariable Long id, @RequestBody Example exampleDetails) {
        return exampleService.updateExample(id, exampleDetails);
    }

    // DELETE: 특정 ID의 Example 삭제
    @DeleteMapping("/{id}")
    public void deleteExample(@PathVariable Long id) {
        exampleService.deleteExample(id);
    }

}
