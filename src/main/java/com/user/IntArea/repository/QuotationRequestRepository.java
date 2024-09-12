package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.enums.QuotationProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    List<QuotationRequest> findQuotationRequestById(UUID quotationRequestId);

    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.quotations q " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and q.id=:quotationId and qr.id = :quotationRequestId")
    List<QuotationRequest> findQuotationRequestByIdByCompany(@Param("companyId") UUID companyId, @Param("quotationId") UUID quotationId, @Param("quotationRequestId") UUID quotationRequestId);

    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and qr.id = :quotationRequestId and qr.progress = :progress")
    List<QuotationRequest> findQuotationRequestByIdAndProgressByCompany(@Param("companyId") UUID companyId, @Param("quotationRequestId") UUID quotationRequestId, @Param("progress") QuotationProgress progress);

    @Query("SELECT qr FROM QuotationRequest qr " +
            "JOIN qr.quotations q " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId and q.id = :quotationId")
    QuotationRequest findQuotationRequestByQuotationIdAndCompany(@Param("companyId") UUID companyId, @Param("quotationId") UUID quotationId);

    // admin

    @Query("SELECT DISTINCT qr from QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    Page<QuotationRequest> getAllQuotationRequestTowardCompanyByAdmin(@Param("companyId") UUID companyId, Pageable pageable);


    @Query(value = "WITH Statuses AS (\n" +
            "    SELECT 'APPROVED' as progress\n" +
            "    UNION ALL\n" +
            "    SELECT 'PENDING' as progress\n" +
            ")\n" +
            "SELECT c.companyname, s.progress, COUNT(qr.id) as count\n" +
            "FROM company c\n" +
            "         CROSS JOIN Statuses s\n" +
            "         LEFT JOIN portfolio p ON p.companyid = c.id\n" +
            "         LEFT JOIN quotationrequest qr ON qr.portfolioid = p.id AND qr.progress = s.progress\n" +
            "WHERE c.id = :companyId \n" +
            "GROUP BY c.companyname, s.progress\n" +
            "ORDER BY c.companyname, s.progress;\n", nativeQuery = true)
    List<Object[]> findQuotationRequestCountById(@Param("companyId") UUID companyId);
}
