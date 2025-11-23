package com.oj.platform.components.problem.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.problem.infrastructure.persistence.jpa.entity.ProblemJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemJpaRepository extends JpaRepository<ProblemJpa, Long> {
    Page<ProblemJpa> findByDifficulty(String difficulty, Pageable pageable);
    Page<ProblemJpa> findByCreatedBy(Long createdBy, Pageable pageable);
    Page<ProblemJpa> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<ProblemJpa> findByClassId(Long classId, Pageable pageable);
    Page<ProblemJpa> findByClassIdIn(List<Long> classIds, Pageable pageable);
    Page<ProblemJpa> findByDifficultyAndClassIdIn(String difficulty, List<Long> classIds, Pageable pageable);
    Page<ProblemJpa> findByTitleContainingIgnoreCaseAndClassIdIn(String title, List<Long> classIds, Pageable pageable);
}

