//package org.springframework.ai.mcp.samples.brave;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.openai.api.OpenAiApi;
//
//import org.springframework.boot.json.JsonParseException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class OpenAiDebugService {
//
//    private ObjectMapper objectMapper;
//
//    public void debugService(OpenAiApi.ChatCompletionRequest request) {
//        try {
//            // JSON으로 직렬화
//            String json = objectMapper.writeValueAsString(request);
//            System.out.println("📦 ChatCompletionRequest JSON:\n" + json);
//
//            // 역직렬화 테스트 (원래 request 객체를 파싱해보는 시나리오)
//            OpenAiApi.ChatCompletionRequest parsed = objectMapper.readValue(json, OpenAiApi.ChatCompletionRequest.class);
//
//            System.out.println("✅ 모델: " + parsed.model());
//            System.out.println("✅ 메시지 수: " + parsed.messages().size());
//
//        } catch (JsonProcessingException e) {
//            System.err.println("❌ JSON 파싱 실패: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}