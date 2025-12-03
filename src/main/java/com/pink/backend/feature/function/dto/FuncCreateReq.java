package com.pink.backend.feature.function.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuncCreateReq {

    private String name;
    private String runtime;
    private String code;
}
