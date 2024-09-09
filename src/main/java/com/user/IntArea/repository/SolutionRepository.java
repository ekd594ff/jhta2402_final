package com.user.IntArea.repository;

import com.user.IntArea.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {

    @Query("SELECT s FROM Solution s WHERE s.portfolio.id = :portfolioId")
    List<Solution> findAllByPortfolioId(UUID portfolioId);

    @Transactional
//    @Query("UPDATE FROM Solution s SET isAvaliable = false WHERE s.portfolio.id = :portfolioId")
    void softDeleteAllByPortfolioId(UUID portfolioId);
}
