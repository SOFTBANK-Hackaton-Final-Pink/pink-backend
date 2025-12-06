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
        log.info("SQS 메시지 전송을 시도합니다. Queue: {}, Payload: {}", queueName, messagePayload);
        try {
            sqsTemplate.send(queueName, messagePayload);
            log.info("SQS 메시지 전송에 성공했습니다. Queue: {}", queueName);
        } catch (QueueDoesNotExistException e) {
            log.error("SQS 큐가 존재하지 않습니다 (재시도되지 않음). Queue: {}", queueName, e);
            throw new RuntimeException("SQS queue not found.", e);
        } catch (SqsException e) {
            log.error("SQS 전송 중 서버 측 오류가 발생했습니다 (재시도되지 않음). Status: {}, AWS Error: {}",
                    e.statusCode(), e.awsErrorDetails().errorCode(), e);
            throw new RuntimeException("An error occurred in SQS. Check credentials and permissions.", e);
        } catch (Exception e) {
            log.error("SQS 메시지 전송 중 알 수 없는 오류가 발생했습니다. Queue: {}", queueName, e);
            throw new RuntimeException("Failed to send message to SQS.", e);
        }
    }

    @Recover
    public <T> void recover(SdkClientException e, T messagePayload) {
        log.error("네트워크 등의 문제로 SQS 메시지 전송 재시도에 모두 실패했습니다. Payload: {}", messagePayload, e);
        throw new RuntimeException("Failed to send message to SQS after multiple retries.", e);
    }
}