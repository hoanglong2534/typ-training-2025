package com.oj.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProblemRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Difficulty is required")
    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM, or HARD")
    private String difficulty;

    @NotNull(message = "Time limit is required")
    @Min(value = 100, message = "Time limit must be at least 100ms")
    private Integer timeLimit;

    @NotNull(message = "Memory limit is required")
    @Min(value = 1024, message = "Memory limit must be at least 1024KB")
    private Integer memoryLimit;
}
