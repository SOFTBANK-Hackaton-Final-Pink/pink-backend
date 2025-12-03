package com.pink.backend.feature.function.service;

import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncDeleteRes;
import com.pink.backend.feature.function.entity.Function;
import com.pink.backend.global.exception.CustomException;
import com.pink.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncDeleteService {

    private final FunctionRepository functionRepository;

    public FuncDeleteRes deleteFunction(UUID functionId) {
        Function function = functionRepository.findById(functionId)
                .orElseThrow(() -> new CustomException(ErrorCode.FUNCTION_NOT_FOUND));

        functionRepository.delete(function);
        return FuncDeleteRes.success();
    }
}
