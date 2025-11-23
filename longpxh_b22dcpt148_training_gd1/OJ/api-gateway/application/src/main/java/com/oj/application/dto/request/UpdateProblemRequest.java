package com.oj.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProblemRequest {
    private String title;
    private String description;

    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM, or HARD")
    private String difficulty;

    @Min(value = 100, message = "Time limit must be at least 100ms")
    private Integer timeLimit;

    @Min(value = 1024, message = "Memory limit must be at least 1024KB")
    private Integer memoryLimit;
}
