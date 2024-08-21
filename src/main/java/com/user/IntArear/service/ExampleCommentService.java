package com.user.IntArear.service;

import com.user.IntArear.dto.ExampleCommentDto;
import com.user.IntArear.dto.ExampleCommentResponseDto;
import com.user.IntArear.dto.ExampleResponseDto;
import com.user.IntArear.entity.Example;
import com.user.IntArear.entity.ExampleComment;
import com.user.IntArear.repository.ExampleCommentRepository;
import com.user.IntArear.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExampleCommentService {

    private final ExampleRepository exampleRepository;
    private final ExampleCommentRepository exampleCommentRepository;

    public List<ExampleCommentResponseDto> findByExampleId(UUID exampleId) {
        Example example = exampleRepository.findById(exampleId)
                .orElse(null);

        return exampleCommentRepository.findByExample(example)
                .stream()
                .map(ExampleCommentResponseDto::new)
                .toList();

        /* 위와 같은 코드
        List<ExampleComment> exampleComments = exampleCommentRepository.findByExample(example);
        List<ExampleCommentResponseDto> exampleCommentResponseDtos = new ArrayList<>();

        for (ExampleComment exampleComment : exampleComments) {
            exampleCommentResponseDtos.add(new ExampleCommentResponseDto(exampleComment));
        }
         */
    }

    public ExampleCommentResponseDto save(UUID exampleId, ExampleCommentDto exampleCommentDto) {

        Example example = exampleRepository.findById(exampleId)
                .orElseThrow(() -> new IllegalArgumentException("Example not found"));

        ExampleComment exampleComment = ExampleComment.builder()
                .description(exampleCommentDto.getDescription())
                .example(example)
                .build();

        exampleCommentRepository.save(exampleComment);

        return new ExampleCommentResponseDto(exampleComment);
    }
}
