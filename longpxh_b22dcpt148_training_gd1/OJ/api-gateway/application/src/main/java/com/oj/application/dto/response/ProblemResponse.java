package com.oj.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Long createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
