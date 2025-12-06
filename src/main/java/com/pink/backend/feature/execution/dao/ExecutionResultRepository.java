package com.pink.backend.feature.execution.dao;

import com.pink.backend.feature.execution.entity.ExecutionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExecutionResultRepository extends JpaRepository<ExecutionResult, UUID> {
}
