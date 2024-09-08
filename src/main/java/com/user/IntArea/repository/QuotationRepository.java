package com.user.IntArea.repository;

import com.user.IntArea.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuotationRepository extends JpaRepository<Quotation, UUID> {
}
