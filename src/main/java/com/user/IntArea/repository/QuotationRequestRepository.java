package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.QuotationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, UUID> {
    List<QuotationRequest> findAllByMember(Member member);

    Page<QuotationRequest> findAllByMemberId(UUID memberId, Pageable pageable);

    @Query("SELECT qr FROM QuotationRequest qr WHERE qr.portfolio.id IN :portfolioIds")
    public abstract Page<QuotationRequest> findAllByPortfolioIds(List<UUID> portfolioIds, Pageable pageable);
}
