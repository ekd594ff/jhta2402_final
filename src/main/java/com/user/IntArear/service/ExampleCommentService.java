package com.user.IntArear.service;

import com.user.IntArear.dto.example.ExampleCommentDto;
import com.user.IntArear.dto.example.ExampleCommentResponseDto;
import com.user.IntArear.dto.member.MemberDto;
import com.user.IntArear.entity.Example;
import com.user.IntArear.entity.ExampleComment;
import com.user.IntArear.entity.Member;
import com.user.IntArear.repository.ExampleCommentRepository;
import com.user.IntArear.repository.ExampleRepository;
import com.user.IntArear.repository.MemberRepository;
import com.user.IntArear.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExampleCommentService {

    private final ExampleRepository exampleRepository;
    private final ExampleCommentRepository exampleCommentRepository;
    private final MemberRepository memberRepository;

    public List<ExampleCommentResponseDto> findByExampleId(UUID exampleId) {
        Example example = exampleRepository.findById(exampleId)
                .orElseThrow(() -> new NoSuchElementException("Example not found"));

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

        MemberDto memberDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        Example example = exampleRepository.findById(exampleId)
                .orElseThrow(() -> new NoSuchElementException("Example not found"));

        ExampleComment exampleComment = ExampleComment.builder()
                .description(exampleCommentDto.getDescription())
                .member(member)
                .example(example)
                .build();

        exampleCommentRepository.save(exampleComment);

        return new ExampleCommentResponseDto(exampleComment);
    }
}
