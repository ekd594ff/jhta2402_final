package com.user.IntArear.repository;

import com.user.IntArear.entity.Example;
import com.user.IntArear.entity.ExampleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExampleCommentRepository extends JpaRepository<ExampleComment, UUID> {

    List<ExampleComment> findByExample(Example example);
}
