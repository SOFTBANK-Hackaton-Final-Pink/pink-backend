package com.pink.backend.feature.execution.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionMessage {

    private UUID executionId;
    private UUID functionId;
}
