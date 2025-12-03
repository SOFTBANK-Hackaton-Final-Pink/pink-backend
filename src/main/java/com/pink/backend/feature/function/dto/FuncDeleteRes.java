package com.pink.backend.feature.function.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncDeleteRes {

    private boolean deleted;

    public static FuncDeleteRes success() {
        return FuncDeleteRes.builder()
                .deleted(true)
                .build();
    }
}
