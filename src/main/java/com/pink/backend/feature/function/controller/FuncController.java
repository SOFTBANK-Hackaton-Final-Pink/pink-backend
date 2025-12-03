package com.pink.backend.feature.function.controller;

import com.pink.backend.feature.function.dto.FuncCreateReq;
import com.pink.backend.feature.function.dto.FuncCreateRes;
import com.pink.backend.feature.function.dto.FuncDeleteRes;
import com.pink.backend.feature.function.dto.FuncUpdateCodeReq;
import com.pink.backend.feature.function.dto.FuncUpdateCodeRes;
import com.pink.backend.feature.function.service.FuncCreateService;
import com.pink.backend.feature.function.service.FuncDeleteService;
import com.pink.backend.feature.function.service.FuncUpdateService;
import com.pink.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/functions")
@RequiredArgsConstructor
public class FuncController {

    private final FuncCreateService funcCreateService;
    private final FuncUpdateService funcUpdateService;
    private final FuncDeleteService funcDeleteService;

    @PostMapping
    public ResponseEntity<ApiResponse<FuncCreateRes>> createFunction(@RequestBody FuncCreateReq request) {
        FuncCreateRes response = funcCreateService.createFunction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/{functionId}/code")
    public ResponseEntity<ApiResponse<FuncUpdateCodeRes>> updateFunctionCode(
            @PathVariable UUID functionId,
            @RequestBody FuncUpdateCodeReq request) {
        FuncUpdateCodeRes response = funcUpdateService.updateFunctionCode(functionId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{functionId}")
    public ResponseEntity<ApiResponse<FuncDeleteRes>> deleteFunction(@PathVariable UUID functionId) {
        FuncDeleteRes response = funcDeleteService.deleteFunction(functionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
