package com.pink.backend.global.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageSender {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String queueName;

    @Retryable(
            retryFor = {SdkClientException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public <T> void sendMessage(T messagePayload) {
        log.info("Attempting to send message to SQS. URL: {}, Payload: {}", queueName, messagePayload);
        try {
            sqsTemplate.send(queueName, messagePayload);
        } catch (QueueDoesNotExistException e) {
            log.error("큐가 존재하지 않을 때 (재시도 불필요) URL: {}", queueName, e);
            throw new RuntimeException("SQS queue not found.", e);
        } catch (SqsException e) {
            log.error("AWS 인증/권한 오류 등 SQS 관련 서버사이드 에러 (재시도 불필요) Status Code: {}, AWS Error Code: {}",
                    e.statusCode(), e.awsErrorDetails().errorCode(), e);
            throw new RuntimeException("An error occurred in SQS. Check credentials and permissions.", e);
        } catch (Exception e) {
            log.error("모든 오류를 포착하는 포괄적 예외 처리 URL: {}", queueName, e);
            throw new RuntimeException("Failed to send message to SQS.", e);
        }
    }

    @Recover
    public <T> void recover(SdkClientException e, T messagePayload) {
        log.error("네트워크 오류 재시도 최종 실패 Payload: {}", messagePayload, e);
        throw new RuntimeException("Failed to send message to SQS after multiple retries.", e);
    }
}