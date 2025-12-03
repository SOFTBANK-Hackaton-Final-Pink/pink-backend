package com.pink.backend.feature.function.service;

import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncCreateReq;
import com.pink.backend.feature.function.dto.FuncCreateRes;
import com.pink.backend.feature.function.entity.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncCreateService {

    private final FunctionRepository functionRepository;

    public FuncCreateRes createFunction(FuncCreateReq request) {
        UUID functionId = UUID.randomUUID();

        Function function = Function.builder()
                .id(functionId)
                .name(request.getName())
                .runtime(request.getRuntime())
                .code(request.getCode())
                .latestVersion(1)
                .build();

        Function savedFunction = functionRepository.save(function);
        return FuncCreateRes.from(savedFunction);
    }
}
