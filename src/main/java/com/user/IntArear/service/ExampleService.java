package com.user.IntArear.service;

import com.user.IntArear.entity.Example;
import com.user.IntArear.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ExampleService {

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE: 새로운 Example 생성
    public Example saveExample(Example example) {
        // 패스워드 필드가 있다면 인코딩 (필요에 따라)
        /*if (example.getPassword() != null) {
            example.setPassword(passwordEncoder.encode(example.getPassword()));
        }*/
        return exampleRepository.save(example);
    }

    // READ: 특정 ID로 Example 조회
    public Example getExampleById(Long id) {
        return exampleRepository.getOne(id);
    }

    // READ: 모든 Example 조회
    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

    // UPDATE: 특정 ID의 Example 수정
    public Example updateExample(Long id, Example exampleDetails) {
        Example existingExample = exampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Example not found with id: " + id));

        // 필요한 필드를 업데이트
        existingExample.setName(exampleDetails.getName());
        existingExample.setDescription(exampleDetails.getDescription());

        // 패스워드 업데이트 시 인코딩
        /*if (exampleDetails.getPassword() != null && !exampleDetails.getPassword().isEmpty()) {
            existingExample.setPassword(passwordEncoder.encode(exampleDetails.getPassword()));
        }*/

        // 필요한 다른 필드도 여기에 추가적으로 업데이트 가능
        return exampleRepository.save(existingExample);
    }


    // DELETE: 특정 ID의 Example 삭제
    public void deleteExample(Long id) {
        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Example not found with id: " + id));
        exampleRepository.delete(example);
    }
}
