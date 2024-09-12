package com.user.IntArea.repository;

import com.user.IntArea.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {

    @Query("SELECT s FROM Solution s WHERE s.portfolio.id = :portfolioId")
    List<Solution> findAllByPortfolioId(UUID portfolioId);

    @Modifying
    @Query("UPDATE Solution s SET s.isDeleted = true WHERE s.portfolio.id = :portfolioId")
    void updateIsDeletedByPortfolioId(UUID portfolioId);


    @Query("SELECT s from Solution s " +
            "join s.requestSolutions rs " +
            "join rs.quotationRequest qr " +
            "WHERE qr.id=:QuotationRequestId")
    List<Solution> getSolutionsByQuotationRequestId(UUID QuotationRequestId);

    List<Solution> findByPortfolioId(UUID id);
}
