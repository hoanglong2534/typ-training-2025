package com.oj_cpp.core.infra.persistence.jpa.repository;

import com.oj_cpp.core.infra.persistence.jpa.entity.ProblemJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemJpaRepository extends JpaRepository<ProblemJpa, Long> {
    Optional<ProblemJpa> findByProblemCode(String problemCode);
    List<ProblemJpa> findByClassId(Long classId);
    boolean existsByProblemCode(String problemCode);
}
