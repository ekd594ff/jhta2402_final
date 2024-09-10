package com.user.IntArea.repository;

import com.user.IntArea.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

    @Query("select q from Quotation q where cast(q.id as string ) LIKE %:id% ")
    Page<Quotation> findAllByIdContains(String id, Pageable pageable);

    @Query("select q from Quotation q where cast(q.totalTransactionAmount as string ) LIKE %:totalTransactionAmount% ")
    Page<Quotation> findAllByTotalTransactionAmountContains(String totalTransactionAmount, Pageable pageable);

    @Query("select q from Quotation q where cast(q.progress as string ) LIKE %:progress% ")
    Page<Quotation> findAllByProgressContains(String progress, Pageable pageable);

    @Query("select q from Quotation q where cast(q.createdAt as string ) LIKE %:createdAt% ")
    Page<Quotation> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    @Query("select q from Quotation q where cast(q.updatedAt as string ) LIKE %:updatedAt% ")
    Page<Quotation> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    @Modifying
    @Query("UPDATE Quotation q SET q.progress = :ADMIN_CANCELLED  WHERE q.id IN :ids")
    void updateQuotationById(Iterable<UUID> ids);
}
