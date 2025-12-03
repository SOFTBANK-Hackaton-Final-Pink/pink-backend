package com.pink.backend.feature.execution.dao;

import com.pink.backend.feature.execution.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, UUID> {
    List<Execution> findByFunctionId(UUID functionId);
}
