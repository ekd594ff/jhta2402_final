package com.user.IntArear.repository;

import com.user.IntArear.entity.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExampleRepository extends JpaRepository<Example, UUID> {

    Optional<Example> getExampleById(UUID id);
}