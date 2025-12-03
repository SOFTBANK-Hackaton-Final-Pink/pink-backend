package com.pink.backend.feature.function.service;

import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncUpdateCodeReq;
import com.pink.backend.feature.function.dto.FuncUpdateCodeRes;
import com.pink.backend.feature.function.entity.Function;
import com.pink.backend.global.exception.CustomException;
import com.pink.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncUpdateService {

    private final FunctionRepository functionRepository;

    public FuncUpdateCodeRes updateFunctionCode(UUID functionId, FuncUpdateCodeReq request) {
        Function function = functionRepository.findById(functionId)
                .orElseThrow(() -> new CustomException(ErrorCode.FUNCTION_NOT_FOUND));

        Integer newVersion = function.getLatestVersion() + 1;
        function.updateCode(request.getCode(), newVersion);

        Function updatedFunction = functionRepository.save(function);
        return FuncUpdateCodeRes.from(updatedFunction.getId(), updatedFunction.getLatestVersion(), updatedFunction.getUpdatedAt());
    }
}
