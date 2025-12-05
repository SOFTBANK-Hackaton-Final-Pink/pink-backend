package com.pink.backend.feature.execution.dao;

import com.pink.backend.feature.execution.entity.Execution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, UUID> {

    List<Execution> findByFunctionId(UUID functionId);

    @Query(
            "SELECT e FROM Execution e " +
                    "WHERE e.function.id = :functionId " +
                    "ORDER BY e.updatedAt DESC"
    )
    List<Execution> findRecentExecutions(@Param("functionId") UUID functionId, Pageable pageable);

    @Query(
            "SELECT e FROM Execution e " +
                    "WHERE e.function.id = :functionId " +
                    "AND e.updatedAt < :cursor " +
                    "ORDER BY e.updatedAt DESC"
    )
    List<Execution> findExecutionsBefore(
            @Param("functionId") UUID functionId,
            @Param("cursor") LocalDateTime cursor,
            Pageable pageable);

}
