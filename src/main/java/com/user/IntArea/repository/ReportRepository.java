package com.user.IntArea.repository;

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
            "m.username AS memberName, " +
            "r.sort AS sort, " +
            "r.title AS title, " +
            "r.description AS description, " +
            "r.progress AS progress, " +
            "r.createdAt AS createdAt, " +
            "r.updatedAt AS updatedAt " +
            "FROM Report r " +
            "JOIN Member m ON r.memberId = m.id " +
            "LEFT JOIN Portfolio p ON cast(r.sort AS String )  = 'PORTFOLIO' AND p.id = r.refId " +
            "LEFT JOIN Review r2 ON cast(r.sort AS String ) = 'REVIEW' AND r2.id = r.refId " +
            "WHERE cast(r.sort AS String )  IN ('PORTFOLIO', 'REVIEW')")
    Page<Tuple> findAllReportDto(Pageable pageable);
}
