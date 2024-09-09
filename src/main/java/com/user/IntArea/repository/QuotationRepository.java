package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

    Optional<Quotation> findByQuotationRequestIdAndProgress(UUID quotationRequestId, Progress progress);

    List<Quotation> findAllByQuotationRequestId(UUID quotationRequestId);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    List<Quotation> findAllByCompany(@Param("companyId") UUID companyId);

    List<Quotation> findAllByProgress(Progress progress);

    @Query("SELECT DISTINCT q FROM Quotation q " +
            "JOIN q.quotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and q.progress=:progress")
    List<Quotation> findAllByProgressAndCompany(@Param("progress") Progress progress, @Param("companyId") UUID companyId);

}
