package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Portfolio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
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

    Page<Portfolio> findAllByCompanyAndIsDeletedAndIsActivated(Company company, boolean isDeleted, boolean isActivated, Pageable pageable);

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
            "WHERE c.isApplied = true " +
            "ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Map<String, Object>> getRandomPortfolioInfoDtos(@Param("count") int count);


    // 포트폴리오에 대한 리뷰들에 작성된 평점의 평균순으로 나열
    @Query(value =
            "SELECT " +
                    "    p.id AS portfolioId, " +
                    "    p.title AS title, " +
                    "    c.companyName AS companyName, " +
                    "    ROUND(AVG(r.rate)::numeric, 2) AS avgRate, " +
                    "    (select i.url from image i where i.refid = p.id limit 1) AS thumbnail " +
                    "FROM Portfolio p " +
                    "LEFT JOIN Image i ON i.refId = p.id " +
                    "LEFT JOIN Company c ON p.companyid = c.id " +
                    "LEFT JOIN QuotationRequest qr ON qr.portfolioid = p.id " +
                    "LEFT JOIN Quotation q ON q.quotationrequestid = qr.id " +
                    "LEFT JOIN Review r ON r.quotationid = q.id " +
                    "WHERE 1 = 1 " +
                    "    AND p.isDeleted = false " +
                    "    AND p.isActivated = true " +
                    "    AND q.progress = 'APPROVED' " +
                    "GROUP BY " +
                    "    p.id,c.companyName " +
                    "ORDER BY " +
                    "    AVG(r.rate) DESC, p.createdAt ASC ",
            nativeQuery = true
    )
    List<Map<String, Object>> getRecommendedPortfolioByAvgRate(Pageable pageable);

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

    @Query(value = "WITH latestPortfolios AS (\n" +
            "    SELECT \n" +
            "        p.id AS portfolioId,\n" +
            "        q2.createdat, \n" +
            "        p.title,\n" +
            "        p.description,\n" +
            "        ROUND(AVG(r.rate)::numeric, 2) as rate,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY p.id ORDER BY q2.createdat DESC) AS idx\n" +
            "    FROM portfolio p\n" +
            "    INNER JOIN quotationrequest q ON p.id = q.portfolioid\n" +
            "    INNER JOIN quotation q2 ON q.id = q2.quotationrequestid AND q2.progress = 'APPROVED'\n" +
            "    INNER JOIN review r ON q2.id = r.quotationid\n" +
            "    WHERE p.isactivated = true AND p.isdeleted = false\n" +
            "    GROUP BY p.id, q2.createdat, p.title, p.description\n" +
            "),\n" +
            "portfolioImages AS (\n" +
            "    SELECT \n" +
            "        i.refid AS portfolioId,\n" +
            "        i.url,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY i.refid ORDER BY i.createdat DESC) AS img_idx\n" +
            "    FROM Image i\n" +
            ")\n" +
            "SELECT \n" +
            "    lp.portfolioId as portfolioId, \n" +
            "    lp.createdat, \n" +
            "    lp.title, \n" +
            "    lp.description, \n" +
            "    lp.rate as avgRate, \n" +
            "    pi.url as thumbnail\n" +
            "FROM latestPortfolios lp\n" +
            "INNER JOIN portfolioImages pi ON lp.portfolioId = pi.portfolioId AND pi.img_idx = 1\n" +
            "WHERE lp.idx = 1\n" +
            "ORDER BY lp.createdat DESC\n" +
            "LIMIT :count", nativeQuery = true)
    List<Map<String, Object>> getRecentTransactionPortfolioList(@Param("count") int count);
}