package com.user.IntArea.repository;

import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.QuotationProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.member m " +
            "WHERE m.id = :memberId")
    Page<Quotation> GetAllByMemberId(@Param("memberId") UUID memberId, Pageable pageable);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.member m " +
            "WHERE m.id = :memberId and q.progress = :progress")
    Page<Quotation> GetAllByMemberIdAndProgress(@Param("memberId") UUID memberId, @Param("progress") QuotationProgress progress, Pageable pageable);


    Optional<Quotation> findByQuotationRequestIdAndProgress(UUID quotationRequestId, QuotationProgress progress);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE qr.id = :quotationRequestId and q.progress = :progress")
    List<Quotation> getAllListByQuotationRequestIdAndProgress(UUID quotationRequestId, QuotationProgress progress);

    List<Quotation> findAllByQuotationRequestId(UUID quotationRequestId);


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

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
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

}

