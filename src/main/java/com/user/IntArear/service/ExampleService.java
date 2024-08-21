package com.user.IntArear.service;

import com.user.IntArear.dto.ExampleDto;
import com.user.IntArear.dto.ExampleResponseDto;
import com.user.IntArear.dto.MemberResponseDto;
import com.user.IntArear.entity.Example;
import com.user.IntArear.entity.Member;
import com.user.IntArear.repository.ExampleCommentRepository;
import com.user.IntArear.repository.ExampleRepository;
import com.user.IntArear.repository.MemberRepository;
import com.user.IntArear.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;
    private final ExampleCommentRepository exampleCommentRepository;
    private final MemberRepository memberRepository;

    // CREATE: 새로운 Example 생성
    public ExampleResponseDto saveExample(ExampleDto exampleDto) {

        MemberResponseDto memberResponseDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("member not found"));

        Member member = memberRepository.findByEmail(memberResponseDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("member not found"));

        Example example = new Example(
                exampleDto.getName(),
                exampleDto.getDescription(),
                member
        );

        /* 아래 코드와 위 코드는 같은 결과 수행
        Example example = Example.builder()
                .name(exampleDto.getName())
                .description(exampleDto.getDescription())
                .build();
         */

        // 패스워드 필드가 있다면 인코딩 (필요에 따라)
        exampleRepository.save(example);

        return new ExampleResponseDto(example);
    }

    // READ: 특정 ID로 Example 조회
    public ExampleResponseDto getExampleById(UUID id) {
        Example example = exampleRepository.getExampleById(id);

        return new ExampleResponseDto(example);
    }

    // READ: 모든 Example 조회
    public List<ExampleResponseDto> getAllExamples() {
        return exampleRepository.findAll()
                .stream()
                .map(ExampleResponseDto::new)
                .toList();

        // 위와 같은 코드
        /*
        List<Example> examples = exampleRepository.findAll();
        List<ExampleResponseDto> exampleResponseDtos = new ArrayList<>();
        for (Example example : examples) {
            exampleResponseDtos.add(new ExampleResponseDto(example));
        }
         */
    }

    // UPDATE: 특정 ID의 Example 수정
    public ExampleResponseDto updateExample(UUID id, ExampleDto exampleDto) {
        // Exception을 던지면, GlobalExceptionHandler에서 필터링 함
        // 임의로 Exception을 던진 후, 나중에 한꺼번에 맞춰서 사용할 예정
        Example existingExample = exampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Example not found with id: " + id));

        // 필요한 필드를 업데이트
        existingExample.setName(exampleDto.getName());
        existingExample.setDescription(exampleDto.getDescription());

        // 필요한 다른 필드도 여기에 추가적으로 업데이트 가능
        Example example = exampleRepository.save(existingExample);
        return new ExampleResponseDto(example);
    }

    // DELETE: 특정 ID의 Example 삭제
    @Transactional
    public void deleteExample(UUID id) {

        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Example not found with id: " + id));

        // 해당 리뷰에 있는 댓글 먼저 삭제
        exampleCommentRepository.deleteByExample(example);
        // 이후 해당 리뷰 삭제
        // 중간에 오류가 나면 댓글만 지운 댓글을 rollback 해야 하므로, @Transactional 활용
        exampleRepository.delete(example);
    }
}