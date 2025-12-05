package com.pink.backend.feature.execution.entity;

import com.pink.backend.feature.common.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "execution_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionResult extends BaseTimeEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID resultId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "execution_id", nullable = false)
  private Execution execution;

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

  @Column
  private Integer durationMs;

  @Builder
  public ExecutionResult(Execution execution, String input, String output,
      String errorMessage, Float cpuUsage, Float memoryUsageMb, Integer durationMs) {
    this.execution = execution;
    this.input = input;
    this.output = output;
    this.errorMessage = errorMessage;
    this.cpuUsage = cpuUsage;
    this.memoryUsageMb = memoryUsageMb;
    this.durationMs = durationMs;
  }
}

