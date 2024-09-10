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
    Page<Quotation> GetAllQuotationsTowardMember(@Param("memberId") UUID memberId, Pageable pageable);

    Optional<Quotation> findByQuotationRequestIdAndProgress(UUID quotationRequestId, QuotationProgress progress);

    List<Quotation> findAllByQuotationRequestId(UUID quotationRequestId);


    // seller

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    List<Quotation> findAllByCompany(@Param("companyId") UUID companyId);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    Page<Quotation> findAllByCompany(@Param("companyId") UUID companyId, Pageable pageable);

    List<Quotation> findAllByProgress(QuotationProgress progress);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and q.progress=:progress")
    List<Quotation> findAllByProgressAndCompany(@Param("progress") QuotationProgress progress, @Param("companyId") UUID companyId);


}
