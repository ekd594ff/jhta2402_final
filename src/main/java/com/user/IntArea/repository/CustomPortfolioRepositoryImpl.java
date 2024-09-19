package com.user.IntArea.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPortfolioRepositoryImpl implements CustomPortfolioRepository {
    @PersistenceContext
    private EntityManager em;


    public Page<Tuple> findPortfolioBySearchWord(String searchWord, String sortField, String sortDirection, Pageable pageable) {
        // 기본 쿼리
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.id AS id, p.title AS title, c.companyName AS companyName, p.description AS description, ")
                .append("(SELECT array_agg(url ORDER BY url) FROM ( ")
                .append("  SELECT DISTINCT i.url ")
                .append("  FROM image i ")
                .append("  WHERE i.refId = p.id ")
                .append("  ORDER BY i.url ")
                .append("  LIMIT 8 ")
                .append(") AS limited_images) AS imageUrls, ")
                .append("ROUND(AVG(re.rate)::numeric, 2) AS rate ")
                .append("FROM portfolio p ")
                .append("JOIN company c ON c.id = p.companyid ")
                .append("JOIN image i ON i.refId = p.id ")
                .append("JOIN quotationrequest qr ON p.id = qr.portfolioid ")
                .append("JOIN quotation q ON qr.id = q.quotationrequestid ")
                .append("JOIN review re ON q.id = re.quotationid ")
                .append("WHERE p.isdeleted = false ")
                .append("AND (p.title LIKE CONCAT('%', :searchWord, '%') OR ")
                .append("p.description LIKE CONCAT('%', :searchWord, '%') OR ")
                .append("c.companyName LIKE CONCAT('%', :searchWord, '%')) ")
                .append("GROUP BY p.id, p.title, c.companyName, p.description ");

        // 동적 정렬 추가
        if (sortField != null && !sortField.isEmpty() && sortDirection != null && !sortDirection.isEmpty()) {
            query.append("ORDER BY ").append(sortField).append(" ").append(sortDirection).append(" ");
        }

        // 페이지네이션 처리
        query.append("OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY");

        TypedQuery<Tuple> typedQuery = (TypedQuery<Tuple>) em.createNativeQuery(query.toString(), Tuple.class);
        typedQuery.setParameter("searchWord", searchWord);
        typedQuery.setParameter("offset", pageable.getOffset());
        typedQuery.setParameter("pageSize", pageable.getPageSize());

        // 결과 조회
        List<Tuple> results = typedQuery.getResultList();

        // 총 개수 계산
        long total = getTotalCount(searchWord);

        return new PageImpl<>(results, pageable, total);
    }

    private long getTotalCount(String searchWord) {
        String countQuery = "SELECT COUNT(*) FROM portfolio p " +
                "JOIN company c ON c.id = p.companyid " + // 회사와 조인 추가
                "WHERE p.isdeleted = false " +
                "AND (p.title LIKE CONCAT('%', :searchWord, '%') OR " +
                "p.description LIKE CONCAT('%', :searchWord, '%') OR " +
                "c.companyName LIKE CONCAT('%', :searchWord, '%'))"; // 괄호 확인

        return ((Number) em.createNativeQuery(countQuery)
                .setParameter("searchWord", searchWord)
                .getSingleResult()).longValue(); // 결과를 Number로 변환
    }
}
