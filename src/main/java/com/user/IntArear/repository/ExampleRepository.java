package com.user.IntArear.repository;

import com.user.IntArear.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExampleRepository extends JpaRepository<Example, UUID> {

    Example getExampleById(UUID id);
}