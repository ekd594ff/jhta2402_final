package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Page<Company> getCompanyByIsApplied(Boolean isApplied, Pageable pageable);

    Optional<Company> getCompanyByMember(Member member);

    Page<Company> findAll(Pageable pageable);

    Page<Company> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    Page<Company> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    Page<Company> findAllByAddressContains(String address, Pageable pageable);

    Page<Company> findAllByDetailAddressContains(String address, Pageable pageable);

    Page<Company> findAllByPhoneContains(String phone, Pageable pageable);

    Page<Company> findAllByCompanyNameContains(String companyName, Pageable pageable);

    Page<Company> findAllByDescriptionContains(String description, Pageable pageable);

    Page<Company> findAllByIsAppliedIs(boolean isApplied, Pageable pageable);

    @Query("SELECT c FROM Company c " +
            "LEFT JOIN c.portfolios p " +
            "LEFT JOIN p.quotationRequests qr " + //join 문제
            "LEFT JOIN qr.quotations q " + //join 문제
            "WHERE 1 = 1 " +
            "AND c.isApplied = true " +
            "AND c.isDeleted = false " +
            "AND q.progress = 'APPROVED' " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(qr.progress) DESC ")
    List<Company> findTop8CompaniesByQuotationCount(Pageable pageable);

    @Modifying
    @Query("UPDATE Company c SET c.isDeleted = true WHERE c.id IN :ids")
    void softDeleteByIds(Iterable<UUID> ids);
}
