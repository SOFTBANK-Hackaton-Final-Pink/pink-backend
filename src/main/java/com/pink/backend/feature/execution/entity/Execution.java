package com.pink.backend.feature.execution.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "executions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Execution {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID executionId;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID functionId;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String codeSnapshot;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(columnDefinition = "JSON")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column
    private Float cpuUsage;

    @Column
    private Float memoryUsageMb;

    @Column(nullable = false)
    private Integer durationMs;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
