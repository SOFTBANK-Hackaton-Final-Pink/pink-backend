package com.pink.backend.feature.execution.controller;

import com.pink.backend.feature.execution.service.FuncInvokeService;
import com.pink.backend.feature.function.dto.FuncInvokeRes;
import com.pink.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Execution", description = "함수 실행 API")
@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecController {
  private final FuncInvokeService funcInvokeService;

  @Operation(summary = "함수 실행 요청", description = "함수 실행을 요청하고 SQS에 함수 실행 요청 메시지를 전송합니다. 성공 시 202 코드와 execution id를 반환합니다.")
  @PostMapping("/{functionId}/invoke")
  public ResponseEntity<ApiResponse<FuncInvokeRes>> invokeFunction(
      @Parameter(description = "함수 ID", required = true) @PathVariable UUID functionId) {
    FuncInvokeRes response = funcInvokeService.invokeFunction(functionId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success(response));
  }
}
