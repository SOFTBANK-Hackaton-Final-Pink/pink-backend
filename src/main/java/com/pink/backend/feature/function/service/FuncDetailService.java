package com.pink.backend.feature.function.service;

import com.pink.backend.feature.execution.dao.ExecutionRepository;
import com.pink.backend.feature.execution.entity.Execution;
import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncDeleteRes;
import com.pink.backend.feature.function.dto.FuncDetailRes;
import com.pink.backend.feature.function.entity.Function;
import com.pink.backend.global.exception.CustomException;
import com.pink.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncDetailService {

    private static final int PAGE_SIZE = 10;

    private final FunctionRepository functionRepository;
    private final ExecutionRepository executionRepository;

    public FuncDetailRes getFunctionDetail(UUID functionId, LocalDateTime cursor) {
        Function function = functionRepository.findById(functionId)
                .orElseThrow(() -> new CustomException(ErrorCode.FUNCTION_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<Execution> executions;

        if (cursor == null) {
            executions = executionRepository.findRecentExecutions(functionId, pageable);
        } else {
            executions = executionRepository.findExecutionsBefore(functionId, cursor, pageable);
        }

        // Execution → ExecutionSummaryDto 로 변환
        List<FuncDetailRes.ExecutionSummaryDto> executionDtos = executions.stream()
                .map(FuncDetailRes.ExecutionSummaryDto::from)
                .toList();

        return FuncDetailRes.builder()
                .functionId(function.getId())
                .name(function.getName())
                .runtime(function.getRuntime())
                .code(function.getCode())
                .latestVersion(function.getLatestVersion())
                .createdAt(function.getCreatedAt())
                .updatedAt(function.getUpdatedAt())
                .executions(executionDtos)
                .build();
    }
}
