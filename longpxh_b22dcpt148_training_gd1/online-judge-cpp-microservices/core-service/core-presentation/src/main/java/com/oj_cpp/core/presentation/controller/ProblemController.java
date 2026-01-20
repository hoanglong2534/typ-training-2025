package com.oj_cpp.core.presentation.controller;

import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.application.dto.response.ProblemResponse;
import com.oj_cpp.core.application.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping
    public ResponseEntity<ProblemResponse> createProblem(
            @Valid @RequestBody CreateProblemRequest request,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        ProblemResponse response = problemService.createProblem(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblemById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{problemCode}")
    public ResponseEntity<ProblemResponse> getProblemByCode(@PathVariable String problemCode) {
        ProblemResponse response = problemService.getProblemByCode(problemCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAllProblems() {
        List<ProblemResponse> responses = problemService.getAllProblems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<ProblemResponse>> getProblemsByClassId(@PathVariable Long classId) {
        List<ProblemResponse> responses = problemService.getProblemsByClassId(classId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return Long.parseLong(jwt.getSubject());
        }
        throw new IllegalStateException("Unable to extract user ID from authentication");
    }
}
