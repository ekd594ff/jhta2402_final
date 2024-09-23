package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.Platform;
import com.user.IntArea.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<Member> findAll(Pageable pageable);

    Page<Member> findAllByEmailContains(String email, Pageable pageable);

    Page<Member> findAllByRole(Role role, Pageable pageable);

    Page<Member> findAllByUsernameContains(String username, Pageable pageable);

    Page<Member> findAllByPlatform(Platform platform, Pageable pageable);

    @Query("select m from Member m where 1=1 and CAST(m.platform AS string) like %?1%")
    Page<Member> findAllByPlatformContaining(String platform, Pageable pageable);

    Page<Member> findAllByCreatedAtContains(String createdAt, Pageable pageable);


    Page<Member> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    Page<Member> findAllByIsDeletedIs(boolean isDeleted, Pageable pageable);

    @Modifying
    @Query("UPDATE Member m SET m.isDeleted = true WHERE m.id IN :ids")
    void softDeleteByIds(Iterable<UUID> ids);

    @Modifying
    @Query("update Member m set m.isDeleted = true WHERE m.id = :id")
    void softDeleteById(UUID id);

    @Override
    void deleteAllById(Iterable<? extends UUID> uuids);
}
