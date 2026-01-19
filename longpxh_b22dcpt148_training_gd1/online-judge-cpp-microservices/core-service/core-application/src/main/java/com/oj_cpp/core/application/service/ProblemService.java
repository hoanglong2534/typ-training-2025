package com.oj_cpp.core.application.service;

import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.application.dto.response.ProblemResponse;
import com.oj_cpp.core.domain.model.Problem;
import com.oj_cpp.core.domain.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    @Transactional
    public ProblemResponse createProblem(CreateProblemRequest request, Long userId) {
        if (problemRepository.existsByProblemCode(request.getProblemCode())) {
            throw new IllegalArgumentException("Problem code already exists: " + request.getProblemCode());
        }

        Problem problem = Problem.builder()
                .problemCode(request.getProblemCode())
                .title(request.getTitle())
                .content(request.getContent())
                .level(request.getLevel())
                .timeLimit(request.getTimeLimit())
                .memoryLimit(request.getMemoryLimit())
                .classId(request.getClassId())
                .createdBy(userId)
                .build();

        Problem saved = problemRepository.save(problem);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + id));
        return toResponse(problem);
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemByCode(String problemCode) {
        Problem problem = problemRepository.findByProblemCode(problemCode)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with code: " + problemCode));
        return toResponse(problem);
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getProblemsByClassId(Long classId) {
        return problemRepository.findByClassId(classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProblem(Long id) {
        if (!problemRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Problem not found with id: " + id);
        }
        problemRepository.deleteById(id);
    }

    private ProblemResponse toResponse(Problem problem) {
        return ProblemResponse.builder()
                .id(problem.getId())
                .problemCode(problem.getProblemCode())
                .title(problem.getTitle())
                .content(problem.getContent())
                .level(problem.getLevel())
                .timeLimit(problem.getTimeLimit())
                .memoryLimit(problem.getMemoryLimit())
                .createdBy(problem.getCreatedBy())
                .classId(problem.getClassId())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .build();
    }
}
