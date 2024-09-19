package com.user.IntArea.repository;

import com.user.IntArea.entity.Portfolio;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPortfolioRepository {
    Page<Tuple> findPortfolioBySearchWord(String searchWord, String sortField, String sortDirection, Pageable pageable);

//    List<Tuple> getRecommendedPortfolioByAvgRate();
}
