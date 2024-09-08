package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Page<Company> getCompanyByIsApplied(Boolean isApplied, Pageable pageable);

    Optional<Company> getCompanyByMember(Member member);

    Page<Company> findAll(Pageable pageable);

    Page<Company> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    Page<Company> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    Page<Company> findAllByAddressContains(String address, Pageable pageable);

    Page<Company> findAllByPhoneContains(String phone, Pageable pageable);

    Page<Company> findAllByCompanyNameContains(String companyName, Pageable pageable);

    Page<Company> findAllByDescriptionContains(String description, Pageable pageable);

    Page<Company> findAllByIsAppliedIs(boolean isApplied, Pageable pageable);

    @Modifying
    @Query("UPDATE Company c SET c.isDeleted = true WHERE c.id IN :ids")
    void softDeleteByIds(Iterable<UUID> ids);
}
