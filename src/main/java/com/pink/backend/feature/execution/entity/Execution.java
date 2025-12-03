package com.pink.backend.feature.execution.entity;

import com.pink.backend.feature.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "executions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Execution extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
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
}
