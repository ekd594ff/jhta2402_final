package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Query("SELECT p from Portfolio p where p.id = :id and p.isDeleted = :isDeleted and p.isActivated = :isActivated")
    Optional<Portfolio> findByIdAndIsDeletedAndIsActivated(UUID id, boolean isDeleted, boolean isActivated);

    @Query("SELECT p from Portfolio p where p.id = :portfolioId and p.isDeleted = false and p.isActivated = true")
    Optional<Portfolio> getOpenPortfolioByPortfolioId(UUID portfolioId);

    Page<Portfolio> findAllByCompanyAndIsDeleted(Company company, boolean isDeleted, Pageable pageable);

    Page<Portfolio> findAllByCompany(Company company, Pageable pageable);

    @Query("SELECT p from Portfolio p where p.isDeleted = false and p.isActivated = true")
    Page<Portfolio> getOpenPortfolios(Pageable pageable);

    @Query("SELECT DISTINCT p from Portfolio p " +
            "left join Company c on p.company.id = c.id " +
            "left join Solution s on s.portfolio.id = p.id " +
            "where p.title LIKE %:searchWord% or p.description LIKE %:searchWord% or c.companyName LIKE %:searchWord% or s.title LIKE %:searchWord% or s.description LIKE %:searchWord% ")
    Page<Portfolio> getAllPortfoliosWithSearchWordByAdmin(String searchWord, Pageable pageable);

    @Query("SELECT DISTINCT p from Portfolio p " +
            "left join Company c on p.company.id = c.id " +
            "left join Solution s on s.portfolio.id = p.id " +
            "where p.title LIKE %:searchWord% or p.description LIKE %:searchWord% or c.companyName LIKE %:searchWord% or s.title LIKE %:searchWord% or s.description LIKE %:searchWord% " +
            "and p.isActivated = true")
    Page<Portfolio> getOpenPortfoliosWithSearchWord(String searchWord, Pageable pageable);

    @Query("SELECT DISTINCT p from Portfolio p left join Company c on p.company.id = c.id where p.isDeleted = false and p.isActivated = true")
    Page<Portfolio> getOpenPortfoliosOfCompany(UUID companyId, Pageable pageable);

    @Query("SELECT p from Portfolio p left join Company c on p.company.id = c.id where c.id = :companyId and p.id = :id and p.isDeleted = false")
    Portfolio findByIdByCompanyManager(UUID id, UUID companyId);

    @Query(value = "SELECT * FROM portfolio ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Portfolio> getRandomPortfolio(@Param("count") int count);

    @Query(value = "SELECT p.*, i.url as thumbnail, c.companyName as companyName " +
            "FROM Portfolio p " +
            "INNER JOIN Image i on i.refId = p.id " +
            "INNER JOIN Company c on p.companyid =  c.id " +
            "ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Map<String, Object>> getRandomPortfolioInfoDtos(@Param("count") int count);

    @Query(value = "SELECT p.id, p.title, c.companyName, p.description, array_agg(i.url ORDER BY i.url) " +
            "FROM Portfolio p " +
            "INNER JOIN Company c ON c.id = p.companyId " +
            "LEFT JOIN Image i ON i.refId = p.id " +
            "WHERE p.isDeleted = false " +
            "AND (p.title LIKE CONCAT('%', :searchWord, '%') OR p.description LIKE CONCAT('%', :searchWord, '%') " +
            "OR c.companyName LIKE CONCAT('%', :searchWord, '%')) " +
            "GROUP BY p.title, c.companyName, p.description, p.createdAt, p.id " +
            "ORDER BY p.createdAt DESC ",
            nativeQuery = true
    )
    Page<Object[]> searchPortfolios(String searchWord, Pageable pageable);

    Page<Portfolio> findAllByTitleContains(String title, Pageable pageable);

    Page<Portfolio> findAllByDescriptionContains(String Description, Pageable pageable);

    @Query("select p from Portfolio p " +
            "join fetch p.company c " +
            "where 1=1 and c.companyName like %:companyName% ")
    Page<Portfolio> findAllByCompanyNameContains(String companyName, Pageable pageable);

    Page<Portfolio> findAllByCreatedAtContains(String createdAt, Pageable pageable);

    Page<Portfolio> findAllByUpdatedAtContains(String updatedAt, Pageable pageable);

    Page<Portfolio> findAllByDeletedIs(boolean Deleted, Pageable pageable);

    @Query("select p from Portfolio p join fetch p.company c")
    Page<Portfolio> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Portfolio p SET p.isDeleted = true WHERE p.id IN :ids")
    void softDeleteByIds(Iterable<UUID> ids);

    @Query("SELECT p FROM Portfolio p WHERE p.company.id = :companyId AND p.isDeleted = false")
    Page<Portfolio> getAllValidByCompanyId (@Param("companyId") UUID companyId, Pageable pageable);

    Page<Portfolio> findAllByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    List<Portfolio> findAllByCompanyId(UUID companyId);
}
