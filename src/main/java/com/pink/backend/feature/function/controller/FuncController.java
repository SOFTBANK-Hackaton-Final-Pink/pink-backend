package com.pink.backend.feature.function.controller;

import com.pink.backend.feature.function.dto.*;
import com.pink.backend.feature.function.service.*;
import com.pink.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Function", description = "서버리스 함수 관리 API")
@RestController
@RequestMapping("/api/functions")
@RequiredArgsConstructor
public class FuncController {

    private final FuncCreateService funcCreateService;
    private final FuncUpdateService funcUpdateService;
    private final FuncDeleteService funcDeleteService;
    private final FuncListService funcListService;
    private final FuncDetailService funcDetailService;

    @Operation(summary = "함수 생성", description = "새로운 서버리스 함수를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<FuncCreateRes>> createFunction(@RequestBody FuncCreateReq request) {
        FuncCreateRes response = funcCreateService.createFunction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "함수 코드 수정", description = "기존 함수의 코드를 수정하고 새 버전을 생성합니다.")
    @PutMapping("/{functionId}/code")
    public ResponseEntity<ApiResponse<FuncUpdateCodeRes>> updateFunctionCode(
            @Parameter(description = "함수 ID", required = true) @PathVariable UUID functionId,
            @RequestBody FuncUpdateCodeReq request) {
        FuncUpdateCodeRes response = funcUpdateService.updateFunctionCode(functionId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "함수 삭제", description = "함수를 삭제합니다.")
    @DeleteMapping("/{functionId}")
    public ResponseEntity<ApiResponse<FuncDeleteRes>> deleteFunction(
            @Parameter(description = "함수 ID", required = true) @PathVariable UUID functionId) {
        FuncDeleteRes response = funcDeleteService.deleteFunction(functionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "함수 목록 조회", description = "함수 목록을 커서 기반 페이지네이션으로 조회합니다. 페이지 크기는 10으로 고정됩니다. 마지막 항목의 updatedAt 값을 다음\n"
        + "요청의 cursor 파라미터로 사용하면 됩니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FuncListItemDto>>> getFunctionList(
        @Parameter(description = "커서 (updatedAt 기준, 형식: yyyy-MM-dd'T'HH:mm:ss)", required = false)
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime cursor) {
        List<FuncListItemDto> response = funcListService.getFunctionList(cursor);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "함수 상세 조회", description = "함수 상세 내역과 함수 실행 이력 목록을 커서 기반 페이지네이션으로 조회합니다. 페이지 크기는 10으로 고정됩니다. 마지막 항목의 updatedAt 값을 다음\n"
            + "요청의 cursor 파라미터로 사용하면 됩니다")
    @GetMapping("/{functionId}")
    public ResponseEntity<ApiResponse<FuncDetailRes>> getFunctionDetail(
            @Parameter(description = "Function ID", required = true)
            @PathVariable UUID functionId,

            @Parameter(description = "커서 (updatedAt 기준, 형식: yyyy-MM-dd'T'HH:mm:ss)", required = false)
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime cursor) {
        FuncDetailRes response = funcDetailService.getFunctionDetail(functionId, cursor);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
