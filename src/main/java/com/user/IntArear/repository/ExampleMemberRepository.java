package com.user.IntArear.repository;

import com.user.IntArear.entity.ExampleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExampleMemberRepository extends JpaRepository<ExampleMember, UUID> {
}
