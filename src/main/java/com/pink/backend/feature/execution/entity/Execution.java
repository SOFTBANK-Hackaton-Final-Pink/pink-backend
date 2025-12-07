package com.pink.backend.feature.execution.entity;

import com.pink.backend.feature.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "executions", indexes = {
    @Index(name = "idx_execution_function_updated", columnList = "function_id, updated_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Execution extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "execution_id", columnDefinition = "BINARY(16)")
    private UUID executionId;

    @Column(name = "function_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID functionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExecutionStatus status;

    @OneToOne(mappedBy = "execution", fetch = FetchType.LAZY)
    private ExecutionResult result;

    @Builder
    public Execution(UUID functionId, ExecutionStatus status) {
        this.functionId = functionId;
        this.status = (status != null) ? status : ExecutionStatus.PENDING;
    }

    public void updateStatus(ExecutionStatus newStatus) {
        this.status = newStatus;
    }
}
