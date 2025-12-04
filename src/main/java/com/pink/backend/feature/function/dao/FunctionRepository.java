package com.pink.backend.feature.function.dao;

import com.pink.backend.feature.function.entity.Function;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FunctionRepository extends JpaRepository<Function, UUID> {

    @Query("SELECT f FROM Function f WHERE f.updatedAt < :cursor ORDER BY f.updatedAt DESC")
    List<Function> findByCursorBefore(@Param("cursor") LocalDateTime cursor, Pageable pageable);

    @Query("SELECT f FROM Function f ORDER BY f.updatedAt DESC")
    List<Function> findAllOrderByUpdatedAtDesc(Pageable pageable);
}
