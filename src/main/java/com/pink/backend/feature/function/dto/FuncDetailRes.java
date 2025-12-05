package com.pink.backend.feature.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pink.backend.feature.execution.entity.Execution;
import com.pink.backend.feature.execution.entity.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncDetailRes {

    private UUID functionId;
    private String name;
    private String runtime;
    private Integer latestVersion;
    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // executions 테이블 요약 목록
    private List<ExecutionSummaryDto> executions;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExecutionSummaryDto {
        private UUID executionId;
        private String status;              // FAILURE, PENDING, RUNNING, SUCCESS

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;

        private ExecutionResultDto executionResult;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ExecutionResultDto {
            private String input;
            private String output;
            private String errorMessage;
            private Float cpuUsage;
            private Float memoryUsageMb;
            private Integer durationMs;
        }

        public static ExecutionSummaryDto from (Execution execution) {
            ExecutionResult result = execution.getResult();

            return ExecutionSummaryDto.builder()
                    .executionId(execution.getExecutionId())
                    .status(execution.getStatus().name())
                    .createdAt(execution.getCreatedAt())
                    .updatedAt(execution.getUpdatedAt())
                    .executionResult(result != null ? ExecutionResultDto.builder()
                            .input(result.getInput())
                            .output(result.getOutput())
                            .errorMessage(result.getErrorMessage())
                            .cpuUsage(result.getCpuUsage())
                            .memoryUsageMb(result.getMemoryUsageMb())
                            .durationMs(result.getDurationMs())
                            .build() : null)
                    .build();
        }
    }
}
