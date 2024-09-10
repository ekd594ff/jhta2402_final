package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, UUID> {
    List<QuotationRequest> findAllByMember(Member member);

    Page<QuotationRequest> findAllByMemberId(UUID memberId, Pageable pageable);

    @Query("SELECT qr FROM QuotationRequest qr WHERE qr.portfolio.id IN :portfolioIds")
    public abstract Page<QuotationRequest> findAllByPortfolioIds(List<UUID> portfolioIds, Pageable pageable);

    Page<QuotationRequest> findAllByMember(Member member, Pageable pageable);

    Page<QuotationRequest> findAllByMemberAndProgress(Member member, QuotationProgress progress, Pageable pageable);

    // seller

    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    Page<QuotationRequest> getAllQuotationRequestTowardCompany(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and qr.progress = :progress")
    Page<QuotationRequest> getAllQuotationRequestTowardCompanySortedByProgress(@Param("companyId") UUID companyId, @Param("progress") QuotationProgress progress, Pageable pageable);

    Optional<QuotationRequest> findQuotationRequestById(UUID quotationRequestId);


    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and qr.id = :quotationRequestId and qr.progress = :progress")
    Optional<QuotationRequest> findQuotationRequestByIdAndProgressByCompany(@Param("companyId") UUID companyId, @Param("quotationRequestId") UUID quotationRequestId, @Param("progress") QuotationProgress progress);

    // admin

    @Query("SELECT DISTINCT qr from QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    Page<QuotationRequest> getAllQuotationRequestTowardCompanyByAdmin(@Param("companyId")UUID companyId, Pageable pageable);

}
