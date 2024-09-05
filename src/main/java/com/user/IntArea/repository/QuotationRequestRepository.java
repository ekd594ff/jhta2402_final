package com.user.IntArea.repository;

import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.QuotationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, UUID> {
    List<QuotationRequest> findAllByMember(Member member);


    @Query("SELECT DISTINCT qr FROM QuotationRequest qr " +
            "JOIN qr.portfolio p " +
            "JOIN p.company c " +
            "WHERE c.id = :companyId")
    List<QuotationRequest> getAllQuotationRequestTowardCompany(UUID id);
}
