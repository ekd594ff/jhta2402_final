package com.user.IntArea.repository;

import com.user.IntArea.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {


}
