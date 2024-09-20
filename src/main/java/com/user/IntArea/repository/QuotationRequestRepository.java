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
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, UUID> {

    List<QuotationRequest> findByPortfolioId(UUID id);

    @Query("select qr from QuotationRequest qr left join Quotation q where q.id = :quotationId")
    Optional<QuotationRequest> getByQuotationId(@Param("quotationId") UUID quotationId);

    List<QuotationRequest> findAllByMember(Member member);

    Page<QuotationRequest> findAllByMemberId(UUID memberId, Pageable pageable);

    @Query("SELECT qr FROM QuotationRequest qr WHERE qr.member.id = :memberId AND qr.progress IN :progresses")
    Page<QuotationRequest> findAllByMemberIdAndProgresses(UUID memberId, List<QuotationProgress> progresses, Pageable pageable);

    @Query("SELECT qr FROM QuotationRequest qr WHERE qr.portfolio.id IN :portfolioIds AND qr.progress IN :progresses")
    Page<QuotationRequest> findAllByPortfolioIdsAndProgress(List<UUID> portfolioIds, List<QuotationProgress> progresses, Pageable pageable);

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
