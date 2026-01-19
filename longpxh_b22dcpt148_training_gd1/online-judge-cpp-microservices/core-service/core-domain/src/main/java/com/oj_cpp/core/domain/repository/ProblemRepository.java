package com.oj_cpp.core.domain.repository;

import com.oj_cpp.core.domain.model.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository {
    Problem save(Problem problem);
    Optional<Problem> findById(Long id);
    Optional<Problem> findByProblemCode(String problemCode);
    List<Problem> findAll();
    List<Problem> findByClassId(Long classId);
    void deleteById(Long id);
    boolean existsByProblemCode(String problemCode);
}
