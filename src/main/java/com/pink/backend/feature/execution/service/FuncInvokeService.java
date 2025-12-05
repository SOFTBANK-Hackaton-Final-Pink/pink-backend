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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncInvokeService {

    private final FunctionRepository functionRepository;
    private final ExecutionRepository executionRepository;
    private final SqsMessageSender sqsMessageSender;

    @Transactional
    public FuncInvokeRes invokeFunction(UUID functionId) {

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
            }
        });

        return new FuncInvokeRes(savedExecution.getExecutionId(), savedExecution.getStatus());
    }
}
