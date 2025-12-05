package com.pink.backend.feature.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pink.backend.feature.execution.entity.Execution;
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

        public static ExecutionSummaryDto from (Execution execution) {
            return ExecutionSummaryDto.builder()
                    .executionId(execution.getExecutionId())
                    .status(execution.getStatus())
                    .createdAt(execution.getCreatedAt())
                    .updatedAt(execution.getUpdatedAt())
                    .build();
        }
    }
}
