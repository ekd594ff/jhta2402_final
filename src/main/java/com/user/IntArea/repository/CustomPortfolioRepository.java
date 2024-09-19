package com.user.IntArea.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPortfolioRepository {
    Page<Tuple> findPortfolioBySearchWord(String searchWord,String sortField, String sortDirection, Pageable pageable);
}
