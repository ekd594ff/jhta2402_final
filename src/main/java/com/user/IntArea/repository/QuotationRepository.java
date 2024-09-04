package com.user.IntArea.repository;

import com.user.IntArea.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {


    @Query("SELECT DISTINCT q from Quotation q left join QuotationRequest qr on q.quotationRequest.id == qr.id where q.isAvailable = true")
    Quotation getQuotationForQuotationRequest();
}
