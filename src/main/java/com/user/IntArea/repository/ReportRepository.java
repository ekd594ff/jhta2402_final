package com.user.IntArea.repository;

import com.user.IntArea.dto.report.ReportDto;
import com.user.IntArea.dto.report.ReportResponseDto;
import com.user.IntArea.entity.Report;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW')")
    Page<Tuple> findAllReportDto(Pageable pageable);

    @Query("SELECT new com.user.IntArea.dto.report.ReportDto(r.id, r.refId, r.memberId, r.sort, r.title, r.description, r.createdAt, r.progress, r.comment) " +
            "FROM Report r JOIN Member m ON r.memberId = m.id " +
            "WHERE r.memberId = :memberId " +
            "ORDER BY r.createdAt DESC")
    Page<ReportDto> findAllByMemberId(UUID memberId, Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and m.username like %:username% ")
    Page<Tuple> findAllByUsernameContains(String username,Pageable pageable);


    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and CAST(r.sort AS string )  like %:sort% ")
    Page<Tuple> findAllBySortContains(String sort,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and CAST(r.createdAt AS string )  like %:createdAt% ")
    Page<Tuple> findAllByCreatedAtContains(String createdAt,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and CAST(r.updatedAt AS string )  like %:updatedAt% ")
    Page<Tuple> findAllByUpdatedAtContains(String updatedAt,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and r.title like %:title% ")
    Page<Tuple> findAllByTitleContains(String title,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and r.description like %:description% ")
    Page<Tuple> findAllByDescriptionContains(String description,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and CAST(r.progress AS string ) like %:progress% ")
    Page<Tuple> findAllByProgressContains(String progress,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and r.comment like %:comment% ")
    Page<Tuple> findAllByCommentContains(String comment,Pageable pageable);

    @Query("SELECT r.id AS id, " +
            "CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END AS refTitle, " +
            "m.username AS username, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.comment AS comment, "+
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE 1=1 " +
            "and cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW') " +
            "and (CASE " +
            "   WHEN r.sort = 'PORTFOLIO' THEN p.title " +
            "   WHEN r.sort = 'REVIEW' THEN r2.title " +
            "END) LIKE %:refTitle%")
    Page<Tuple> findAllByRefTitleContains(String refTitle,Pageable pageable);
}
