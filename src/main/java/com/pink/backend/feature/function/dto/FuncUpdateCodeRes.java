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
public class FuncUpdateCodeRes {

    private UUID functionId;
    private Integer newVersion;

    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static FuncUpdateCodeRes from(UUID functionId, Integer newVersion, LocalDateTime updatedAt) {
        return FuncUpdateCodeRes.builder()
                .functionId(functionId)
                .newVersion(newVersion)
                .updatedAt(updatedAt)
                .build();
    }
}
