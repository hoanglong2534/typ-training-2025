package com.oj.application.controller;

import com.oj.application.dto.request.CreateProblemRequest;
import com.oj.application.dto.request.UpdateProblemRequest;
import com.oj.application.dto.response.ProblemResponse;
import com.oj.application.security.RequestAuthContext;
import com.oj.platform.components.problem.domain.model.Problem;
import com.oj.platform.components.problem.domain.service.ProblemService;
import com.oj.platform.core.domain.pagination.PageResult;
import com.oj.platform.core.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;
    private final RequestAuthContext authContext;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProblemResponse> create(@Valid @RequestBody CreateProblemRequest request) {
        Long userId = authContext.currentUserId();
        if (userId == null) {
            return ApiResponse.error("User not authenticated");
        }

        Problem problem = Problem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .timeLimit(request.getTimeLimit())
                .memoryLimit(request.getMemoryLimit())
                .createdBy(userId)
                .build();

        Problem created = problemService.create(problem);
        ProblemResponse response = toResponse(created);

        return ApiResponse.success(response, "Problem created successfully");
    }

    @GetMapping
    public ApiResponse<List<ProblemResponse>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "difficulty", required = false) String difficulty,
            @RequestParam(name = "search", required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        
        Page<Problem> problems;

        if (difficulty != null && !difficulty.isEmpty()) {
            problems = problemService.findByDifficulty(difficulty, pageable);
        } else if (search != null && !search.isEmpty()) {
            problems = problemService.searchByTitle(search, pageable);
        } else {
            problems = problemService.findAll(pageable);
        }

        // Convert to PageResult and use fromPage() for proper pagination metadata
        PageResult<ProblemResponse> result = PageResult.of(
                problems.getContent().stream().map(this::toResponse).toList(),
                (int) problems.getTotalElements(),
                page,
                size
        );

        return ApiResponse.fromPage(result, "Problems retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<ProblemResponse> getById(@PathVariable Long id) {
        Problem problem = problemService.findById(id);
        
        // Check if user has access to this problem (belongs to one of user's classes)
        List<Long> userClassIds = authContext.currentUserClassIds();
        if (problem.getClassId() != null && !userClassIds.contains(problem.getClassId())) {
            return ApiResponse.error("You don't have access to this problem");
        }
        
        ProblemResponse response = toResponse(problem);
        return ApiResponse.success(response, "Problem retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProblemResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateProblemRequest request) {

        Problem existing = problemService.findById(id);

        if (request.getTitle() != null) existing.setTitle(request.getTitle());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());
        if (request.getDifficulty() != null) existing.setDifficulty(request.getDifficulty());
        if (request.getTimeLimit() != null) existing.setTimeLimit(request.getTimeLimit());
        if (request.getMemoryLimit() != null) existing.setMemoryLimit(request.getMemoryLimit());

        Problem updated = problemService.update(id, existing);
        ProblemResponse response = toResponse(updated);

        return ApiResponse.success(response, "Problem updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        problemService.delete(id);
        return ApiResponse.success(null, "Problem deleted successfully");
    }

    private ProblemResponse toResponse(Problem problem) {
        return ProblemResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty())
                .timeLimit(problem.getTimeLimit())
                .memoryLimit(problem.getMemoryLimit())
                .createdBy(problem.getCreatedBy())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .build();
    }
}

