package com.user.IntArea.repository;

import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.entity.enums.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {

    Optional<Quotation> findByQuotationRequestIdAndProgress(UUID quotationRequestId, Progress progress);

    List<Quotation> findAllByQuotationRequestId(UUID quotationRequestId);

    List<Quotation> findAllByCompany(Company company);

    List<Quotation> findAllByProgress(Progress progress);

    List<Quotation> findAllByProgressAndCompany(Progress progress, Company company);

}
