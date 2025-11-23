package com.oj.platform.components.problem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    private Long id;
    private String title;
    private String description;
    private String difficulty; // EASY, MEDIUM, HARD
    private Integer timeLimit; // in milliseconds
    private Integer memoryLimit; // in KB
    private Long createdBy;
    private Long classId; // Class this problem belongs to
    private Instant createdAt;
    private Instant updatedAt;
}
