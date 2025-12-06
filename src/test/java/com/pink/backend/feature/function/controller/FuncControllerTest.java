package com.pink.backend.feature.function.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pink.backend.feature.function.dto.FuncCreateReq;
import com.pink.backend.global.sqs.SqsMessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(FuncControllerTest.TestSqsConfig.class)
class FuncControllerTest {

    @TestConfiguration
    public static class TestSqsConfig {

        @Bean
        public SqsMessageSender sqsMessageSender() {
            return Mockito.mock(SqsMessageSender.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SqsMessageSender sqsMessageSender;

    @Test
    @DisplayName("성공: 함수를 생성하고 즉시 실행 요청을 보낸다")
    void createAndInvokeFunction_Success() throws Exception {
        // given: SQS Sender가 어떤 메시지든 받으면 아무것도 하지 않도록 설정
        doNothing().when(sqsMessageSender).sendMessage(any());

        // when: 함수 생성 요청
        FuncCreateReq createReq = new FuncCreateReq("test-func", "java21", "public class Test {}");
        MvcResult createResult = mockMvc.perform(post("/api/functions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.functionId").exists())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String functionId = objectMapper.readTree(responseBody).get("data").get("functionId").asText();

        // then: 생성된 함수로 실행 요청
        mockMvc.perform(post("/api/executions/" + functionId + "/invoke"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.executionId").exists())
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }
}