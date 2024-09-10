package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.QuotationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, UUID> {
    List<QuotationRequest> findAllByMember(Member member);

    @Query("select q from QuotationRequest q " +
            "join fetch q.member m " +
            "join fetch q.portfolio p ")
    Page<QuotationRequest> findAll(Pageable pageable);
    @Query("select q from QuotationRequest q where cast(q.id as string ) like %:id% ")
    Page<QuotationRequest> findAllByIdContains(String id, Pageable pageable);

    @Query("select q from QuotationRequest q " +
            "join fetch q.member m " +
            "where 1=1 and " +
            "m.username like %:username% ")
    Page<QuotationRequest> findAllByUsernameContains(String username, Pageable pageable);

    @Query("select q from QuotationRequest q " +
            "join fetch q.portfolio p " +
            "where 1=1 and " +
            "cast(p.id as string ) like %:id% ")
    Page<QuotationRequest> findAllByPortfolioIdContains(String id, Pageable pageable);

    @Query("select q from QuotationRequest q where q.title like %:title% ")
    Page<QuotationRequest> findAllByTitleContains(String title, Pageable pageable);

    @Query("select q from QuotationRequest q where q.description like %:description% ")
    Page<QuotationRequest> findAllByDescriptionContains(String description, Pageable pageable);

    @Query("select q from QuotationRequest q where cast(q.progress as string ) like %:progress% ")
    Page<QuotationRequest> findAllByProgressContains(String progress, Pageable pageable);

    @Query("select q from QuotationRequest q where cast(q.createdAt as string ) like %:createdAt% ")
    Page<QuotationRequest> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    @Query("select q from QuotationRequest q where cast(q.updatedAt as string ) like %:updatedAt% ")
    Page<QuotationRequest> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    @Modifying
    @Query("update QuotationRequest q set q.progress = :ADMIN_CANCELLD where q.id in :ids ")
    void updateProgressByIds(List<UUID> ids);
}
