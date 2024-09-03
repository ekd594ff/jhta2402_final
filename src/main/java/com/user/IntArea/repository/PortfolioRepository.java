package com.user.IntArea.repository;

import com.user.IntArea.dto.portfolio.PortfolioInfoDto;
import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Query("SELECT p from Portfolio p where p.id = :id and p.isDeleted = false and 1=1") // (Todo) p.isActivated = true
    Portfolio getOpenPortfolioInfoById(UUID id);
    
    Page<Portfolio> findAllByCompany(Company company, Pageable pageable);

    @Query("SELECT p from Portfolio p where p.isDeleted = false and 1=1") // (Todo) p.isActivated = true
    Page<Portfolio> getOpenPortfolios(Pageable pageable);

    @Query("SELECT DISTINCT p from Portfolio p " +
            "left join Company c on p.company.id = c.id " +
            "left join Solution s on s.portfolio.id = p.id " +
            "where p.title LIKE %:searchWord% or p.description LIKE %:searchWord% or c.companyName LIKE %:searchWord% or s.title LIKE %:searchWord% or s.description LIKE %:searchWord% " )
    Page<Portfolio> getAllPortfoliosWithSearchWordByAdmin(String searchWord, Pageable pageable);

    @Query("SELECT DISTINCT p from Portfolio p " +
            "left join Company c on p.company.id = c.id " +
            "left join Solution s on s.portfolio.id = p.id " +
            "where p.title LIKE %:searchWord% or p.description LIKE %:searchWord% or c.companyName LIKE %:searchWord% or s.title LIKE %:searchWord% or s.description LIKE %:searchWord% " )
    Page<Portfolio> getOpenPortfoliosWithSearchWord(String searchWord, Pageable pageable);


    @Query("SELECT DISTINCT p from Portfolio p left join Company c on p.company.id = c.id where p.isDeleted = false and 1=1") // (Todo) p.isActivated = true
    Page<Portfolio> getOpenPortfolioInfoDtosOfCompany(UUID companyId, Pageable pageable);

    @Query("SELECT p from Portfolio p left join Company c on p.company.id = c.id where c.id = :companyId and p.id = :id and p.isDeleted = false and 1=1") // (Todo) p.isActivated = true
    Portfolio findByIdByCompanyManager(UUID id, UUID companyId);

    @Query(value = "SELECT * FROM portfolio ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Portfolio> getRandomPortfolioInfoDtos(@Param("count") int count);
}
