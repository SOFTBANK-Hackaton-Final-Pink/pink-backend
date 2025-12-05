package com.pink.backend.feature.function.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuncCreateReq {

    @NotBlank
    private String name;
    @NotBlank
    private String runtime;
    @NotBlank
    private String code;
}
