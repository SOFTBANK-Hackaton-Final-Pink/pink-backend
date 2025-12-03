package com.pink.backend.feature.function.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncCreateRes {

    private UUID functionId;
    private String name;
    private String runtime;
    private Integer version;

    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static FuncCreateRes from(com.pink.backend.feature.function.entity.Function function) {
        return FuncCreateRes.builder()
                .functionId(function.getId())
                .name(function.getName())
                .runtime(function.getRuntime())
                .version(function.getLatestVersion())
                .createdAt(function.getCreatedAt())
                .updatedAt(function.getUpdatedAt())
                .build();
    }
}
