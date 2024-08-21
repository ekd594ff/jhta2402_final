package com.user.IntArear.service;

import com.user.IntArear.dto.ExampleCommentDto;
import com.user.IntArear.entity.Example;
import com.user.IntArear.entity.ExampleComment;
import com.user.IntArear.repository.ExampleCommentRepository;
import com.user.IntArear.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExampleCommentService {

    private final ExampleRepository exampleRepository;
    private final ExampleCommentRepository exampleCommentRepository;

    public List<ExampleComment> findByExampleId(UUID exampleId) {
        Example example = exampleRepository.findById(exampleId)
                .orElse(null);

        return exampleCommentRepository.findByExample(example);
    }

    public ExampleComment save(UUID exampleId, ExampleCommentDto exampleCommentDto) {

        Example example = exampleRepository.findById(exampleId)
                .orElseThrow(() -> new IllegalArgumentException("Example not found"));

        ExampleComment exampleComment = ExampleComment.builder()
                .description(exampleCommentDto.getDescription())
                .example(example)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return exampleCommentRepository.save(exampleComment);
    }
}
