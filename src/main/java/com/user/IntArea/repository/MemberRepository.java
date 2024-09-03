package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
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

    Page<Member> findAllByEmailContains(String email, Pageable pageable);

    Page<Member> findAllByRole(Role role, Pageable pageable);

    Page<Member> findAllByUsernameContains(String username, Pageable pageable);

    Page<Member> findAllByPlatform(Platform platform, Pageable pageable);

    Page<Member> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    Page<Member> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    Page<Member> findAllByIsDeletedIs(boolean isDeleted, Pageable pageable);


//    @Query("SELECT m FROM Member m WHERE 1=1 and m.email LIKE %:filterValue%")
//    Page<Member> findByEmailContaining(String filterColumn, String filterValue);

    @Modifying
    @Query("update Member m set m.isDeleted = true WHERE m.id = %:id")
    void softDeleteById(UUID id);
}
