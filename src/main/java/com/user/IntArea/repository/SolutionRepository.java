package com.user.IntArea.repository;

import com.user.IntArea.entity.Solution;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {

    @Query("SELECT s FROM Solution s WHERE s.portfolio.id = :portfolioId AND s.isDeleted = false")
    List<Solution> findAllByPortfolioId(UUID portfolioId);

    @Modifying
    @Query("UPDATE Solution s SET s.isDeleted = true WHERE s.portfolio.id = :portfolioId")
    void updateIsDeletedByPortfolioId(UUID portfolioId);


    @Query("SELECT s from Solution s " +
            "join s.requestSolutions rs " +
            "join rs.quotationRequest qr " +
            "WHERE qr.id=:QuotationRequestId")
    List<Solution> getSolutionsByQuotationRequestId(UUID QuotationRequestId);

    @Query(value = """
                WITH FirstImage AS (
                    SELECT i.refid, i.url,
                           ROW_NUMBER() OVER (PARTITION BY i.refid ORDER BY i.id) AS rn
                    FROM image i
                )
                SELECT COUNT(s.id) AS solution_count,
                       s.id,
                       s.price,
                       s.title,
                       s.description,
                       p.id AS portfolioId,
                       fi.url AS url
                FROM solution s
                JOIN portfolio p ON s.portfolioid = p.id
                JOIN quotationrequest qr ON qr.portfolioid = p.id
                JOIN quotation q ON q.quotationrequestid = qr.id
                JOIN FirstImage fi ON fi.refid = p.id AND fi.rn = 1
                WHERE s.isdeleted = false
                  AND p.isactivated = true
                  AND p.isdeleted = false
                  AND q.progress = 'APPROVED'
                GROUP BY s.id, p.id, fi.url
                ORDER BY COUNT(s.id) DESC
                LIMIT :count 
            """, nativeQuery = true)
    List<Tuple> getTopMostQuotedSolutions(@Param("count") int count);

    List<Solution> findByPortfolioId(UUID id);
}
