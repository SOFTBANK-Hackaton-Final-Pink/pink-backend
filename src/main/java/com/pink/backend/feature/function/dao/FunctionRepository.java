package com.pink.backend.feature.function.dao;

import com.pink.backend.feature.function.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FunctionRepository extends JpaRepository<Function, UUID> {
}
