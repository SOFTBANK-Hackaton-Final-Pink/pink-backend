package com.pink.backend.feature.function.dto;

import com.pink.backend.feature.execution.entity.ExecutionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Schema(description = "함수 실행 응답 DTO")
public class FuncInvokeRes {

    @Schema(description = "실행 ID", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private final UUID executionId;

    @Schema(description = "실행 상태", example = "PENDING")
    private final ExecutionStatus status;
}
