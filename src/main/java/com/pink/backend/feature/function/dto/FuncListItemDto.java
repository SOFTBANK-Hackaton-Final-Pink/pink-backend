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
public class FuncListItemDto {

    private UUID functionId;
    private String name;
    private String runtime;
    private Integer latestVersion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static FuncListItemDto from(com.pink.backend.feature.function.entity.Function function) {
        return FuncListItemDto.builder()
                .functionId(function.getId())
                .name(function.getName())
                .runtime(function.getRuntime())
                .latestVersion(function.getLatestVersion())
                .updatedAt(function.getUpdatedAt())
                .build();
    }
}
