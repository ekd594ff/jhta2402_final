package com.user.IntArea.repository;

import com.user.IntArea.dto.requestSolution.RequestSolutionDto;
import com.user.IntArea.entity.QuotationRequest;
import com.user.IntArea.entity.RequestSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RequestSolutionRepository extends JpaRepository<RequestSolution, UUID> {

}
