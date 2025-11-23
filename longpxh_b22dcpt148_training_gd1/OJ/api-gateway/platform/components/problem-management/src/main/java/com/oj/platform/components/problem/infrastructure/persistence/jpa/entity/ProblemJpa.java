package com.oj.platform.components.problem.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String difficulty;

    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit;

    @Column(name = "memory_limit", nullable = false)
    private Integer memoryLimit;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
