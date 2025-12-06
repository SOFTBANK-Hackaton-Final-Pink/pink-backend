package com.pink.backend.feature.function.service;

import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncCreateReq;
import com.pink.backend.feature.function.dto.FuncCreateRes;
import com.pink.backend.feature.function.entity.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncCreateService {

    private final FunctionRepository functionRepository;

    public FuncCreateRes createFunction(FuncCreateReq request) {
        log.info("함수 생성을 시도합니다. 이름: {}", request.getName());
        UUID functionId = UUID.randomUUID();

        Function function = Function.builder()
                .id(functionId)
                .name(request.getName())
                .runtime(request.getRuntime())
                .code(request.getCode())
                .latestVersion(1)
                .build();

        Function savedFunction = functionRepository.save(function);
        log.info("함수 생성이 완료되었습니다. 이름: {}, ID: {}", savedFunction.getName(), savedFunction.getId());
        return FuncCreateRes.from(savedFunction);
    }
}
