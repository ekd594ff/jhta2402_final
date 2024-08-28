package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<Member> findAll(Pageable pageable);

    @Modifying
    @Query("update Member m set m.isDeleted = true WHERE m.id = ?1")
    void softDeleteById(UUID id);
}
