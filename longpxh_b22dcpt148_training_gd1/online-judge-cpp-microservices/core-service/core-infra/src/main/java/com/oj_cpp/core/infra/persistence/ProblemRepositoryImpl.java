package com.oj_cpp.core.infra.persistence;

import com.oj_cpp.core.domain.model.Problem;
import com.oj_cpp.core.domain.repository.ProblemRepository;
import com.oj_cpp.core.infra.persistence.jpa.mapper.ProblemMapper;
import com.oj_cpp.core.infra.persistence.jpa.repository.ProblemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {
    private final ProblemJpaRepository problemJpaRepository;
    private final ProblemMapper problemMapper;

    @Override
    public Problem save(Problem problem) {
        var entity = problemMapper.toEntity(problem);
        var saved = problemJpaRepository.save(entity);
        return problemMapper.toDomain(saved);
    }

    @Override
    public Optional<Problem> findById(Long id) {
        return problemJpaRepository.findById(id)
                .map(problemMapper::toDomain);
    }

    @Override
    public Optional<Problem> findByProblemCode(String problemCode) {
        return problemJpaRepository.findByProblemCode(problemCode)
                .map(problemMapper::toDomain);
    }

    @Override
    public List<Problem> findAll() {
        return problemJpaRepository.findAll().stream()
                .map(problemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Problem> findByClassId(Long classId) {
        return problemJpaRepository.findByClassId(classId).stream()
                .map(problemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        problemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByProblemCode(String problemCode) {
        return problemJpaRepository.existsByProblemCode(problemCode);
    }
}
