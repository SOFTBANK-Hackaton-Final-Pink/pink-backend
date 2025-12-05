package com.pink.backend.global.sqs;

import com.pink.backend.feature.execution.dao.ExecutionRepository;
import com.pink.backend.feature.execution.dao.ExecutionResultRepository;
import com.pink.backend.feature.execution.dto.ExecutionMessage;
import com.pink.backend.feature.execution.entity.Execution;
import com.pink.backend.feature.execution.entity.ExecutionResult;
import com.pink.backend.feature.execution.entity.ExecutionStatus;
import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.entity.Function;
import com.pink.backend.global.exception.CustomException;
import com.pink.backend.global.exception.ErrorCode;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsMessageListener {

    private final ExecutionRepository executionRepository;
    private final ExecutionResultRepository executionResultRepository;
    private final FunctionRepository functionRepository;

    @SqsListener("${spring.cloud.aws.sqs.queue-url}")
    @Transactional
    public void receiveMessage(ExecutionMessage message) {
        Execution execution = executionRepository.findById(message.getExecutionId())
                .orElseThrow(() -> new CustomException(ErrorCode.EXECUTION_NOT_FOUND));

        if (execution.getStatus() == ExecutionStatus.SUCCESS || execution.getStatus() == ExecutionStatus.FAILURE) {
            log.warn("Execution {} already completed with status {}. Ignoring duplicate message.",
                    execution.getExecutionId(), execution.getStatus());
            return;
        }

        execution.updateStatus(ExecutionStatus.RUNNING);

        Function function = functionRepository.findById(message.getFunctionId())
                .orElseThrow(() -> new CustomException(ErrorCode.FUNCTION_NOT_FOUND));

        String output = "";
        String errorMessage = "";
        long durationMs = 0;
        boolean isSuccess = false;

        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("execution-");
            String className = extractClassName(function.getCode());
            Path sourceFile = tempDir.resolve(className + ".java");
            Files.writeString(sourceFile, function.getCode());

            long startTime = System.nanoTime();

            Process compileProcess = new ProcessBuilder("javac", sourceFile.toString()).start();
            if (!waitForProcess(compileProcess, 10000)) { // 10 seconds
                compileProcess.destroyForcibly();
                throw new IOException("Compilation timed out.");
            }

            if (compileProcess.exitValue() != 0) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()))) {
                    errorMessage = reader.lines().reduce((a, b) -> a + "\n" + b).orElse("");
                }
            } else {
                Process runProcess = new ProcessBuilder("java", "-cp", tempDir.toString(), className).start();
                if (!waitForProcess(runProcess, 30000)) { // 30 seconds
                    runProcess.destroyForcibly();
                    throw new IOException("Execution timed out.");
                }

                durationMs = (System.nanoTime() - startTime) / 1_000_000L;

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
                    output = reader.lines().reduce((a, b) -> a + "\n" + b).orElse("");
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()))) {
                    errorMessage = reader.lines().reduce((a, b) -> a + "\n" + b).orElse("");
                }

                if (runProcess.exitValue() == 0) {
                    isSuccess = true;
                }
            }
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            log.error("Error during code execution for executionId: {}", execution.getExecutionId(), e);
            errorMessage = e.getMessage();
        } finally {
            if (isSuccess) {
                execution.updateStatus(ExecutionStatus.SUCCESS);
            } else {
                execution.updateStatus(ExecutionStatus.FAILURE);
            }

            ExecutionResult result = ExecutionResult.builder()
                    .execution(execution)
                    .output(output)
                    .errorMessage(errorMessage)
                    .durationMs((int) durationMs)
                    .build();
            executionResultRepository.save(result);

            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .map(Path::toFile)
                            .sorted((o1, o2) -> -o1.compareTo(o2))
                            .forEach(File::delete);
                } catch (IOException e) {
                    log.error("Failed to delete temp directory: {}", tempDir, e);
                }
            }
        }
    }

    private boolean waitForProcess(Process process, long timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long remaining = timeoutMillis;
        while (remaining > 0) {
            try {
                process.exitValue();
                return true;
            } catch (IllegalThreadStateException ex) {
                long sleepTime = Math.min(remaining, 100);
                Thread.sleep(sleepTime);
                remaining = timeoutMillis - (System.currentTimeMillis() - startTime);
            }
        }
        return false;
    }

    private String extractClassName(String code) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+([\\w$]+)");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Could not extract public class name from code.");
    }
}
