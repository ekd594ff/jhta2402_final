package com.user.IntArea.repository;

import com.user.IntArea.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.user.IntArea.entity.enums.QuotationProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

    @Query("SELECT q from Quotation q where q.id = :quotationId")
    Optional<Quotation> getByQuotationId(UUID quotationId);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.member m " +
            "WHERE m.id = :memberId")
    Page<Quotation> GetAllByMemberId(@Param("memberId") UUID memberId, Pageable pageable);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.member m " +
            "WHERE m.id = :memberId and q.progress = :progress")
    Page<Quotation> GetAllByMemberIdAndProgress(UUID memberId, QuotationProgress progress, Pageable pageable);


    Optional<Quotation> findByQuotationRequestIdAndProgress(UUID quotationRequestId, QuotationProgress progress);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE qr.id = :quotationRequestId and q.progress = :progress")
    List<Quotation> getAllListByQuotationRequestIdAndProgress(UUID quotationRequestId, QuotationProgress progress);

    List<Quotation> findAllByQuotationRequestIdOrderByUpdatedAt(UUID quotationRequestId);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company " +
            "WHERE p.id = :portfolioId")
    List<Quotation> getAllByPortfolioId(UUID portfolioId);

    // seller

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    List<Quotation> getAllByCompanyAsList(@Param("companyId") UUID companyId);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    Page<Quotation> getAllByCompany(@Param("companyId") UUID companyId, Pageable pageable);

    Page<Quotation> findAllByProgress(QuotationProgress progress, Pageable pageable);

    @Query("SELECT q FROM Quotation q " +
            "INNER JOIN q.quotationRequest qr " +
            "INNER JOIN  qr.portfolio p " +
            "INNER JOIN  p.company c " +
            "WHERE c.id = :companyId and q.progress=:progress")
    Page<Quotation> getAllByCompanySortedByProgress(@Param("progress") QuotationProgress progress, @Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "JOIN qr.requestSolutions rs " +
            "WHERE c.id = :companyId " +
            "AND (qr.title LIKE %:searchWord% " +
            "OR qr.description LIKE %:searchWord% " +
            "OR c.description LIKE %:searchWord% " +
            "OR CAST(q.totalTransactionAmount AS string) LIKE %:searchWord% " +
            "OR CAST(q.progress AS string) LIKE %:searchWord% " +
            "OR CAST(qr.progress AS string) LIKE %:searchWord% )")
    Page<Quotation> getAllByCompanyWithSearchWord(@Param("searchWord") String searchWord, @Param("companyId") UUID companyId, Pageable pageable);

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

