package com.pink.backend.feature.execution.service;

import com.pink.backend.feature.execution.dao.ExecutionRepository;
import com.pink.backend.feature.execution.dto.ExecutionMessage;
import com.pink.backend.feature.execution.entity.Execution;
import com.pink.backend.feature.execution.entity.ExecutionStatus;
import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncInvokeRes;
import com.pink.backend.feature.function.entity.Function;
import com.pink.backend.global.exception.CustomException;
import com.pink.backend.global.exception.ErrorCode;
import com.pink.backend.global.sqs.SqsMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncInvokeService {

    private final FunctionRepository functionRepository;
    private final ExecutionRepository executionRepository;
    private final SqsMessageSender sqsMessageSender;

    @Transactional
    public FuncInvokeRes invokeFunction(UUID functionId) {
        log.info("함수 실행을 요청합니다. 함수 ID: {}", functionId);

        Function function = functionRepository.findById(functionId)
                .orElseThrow(() -> new CustomException(ErrorCode.FUNCTION_NOT_FOUND));

        Execution execution = Execution.builder()
                .functionId(function.getId())
                .status(ExecutionStatus.PENDING)
                .build();

        Execution savedExecution = executionRepository.save(execution);

        ExecutionMessage message = new ExecutionMessage(
                savedExecution.getExecutionId(),
                savedExecution.getFunctionId()
        );

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sqsMessageSender.sendMessage(message);
                log.info("(FuncInvokeService) 실행 요청을 성공적으로 보냈습니다. 함수 ID: {}, 실행 ID: {}",
                        message.getFunctionId(), message.getExecutionId());
            }
        });

        return new FuncInvokeRes(savedExecution.getExecutionId(), savedExecution.getStatus());
    }
}
