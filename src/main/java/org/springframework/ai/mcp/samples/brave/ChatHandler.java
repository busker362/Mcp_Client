package org.springframework.ai.mcp.samples.brave;

import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest;
//import org.springframework.ai.mcp.samples.brave.OpenAiDebugService;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
//open ai
import org.springframework.ai.mcp.samples.brave.dto.ChatResponseDTO;
import org.springframework.ai.openai.OpenAiChatOptions;
//mistral ai
//import org.springframework.ai.mistralai.MistralAiChatModel;
//import org.springframework.ai.mistralai.MistralAiChatOptions;
//import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ChatHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);
    private final ChatClient chatClient;
    private final Map<String, List<Message>> chatHistories = new ConcurrentHashMap<>();
//    private final OpenAiDebugService openAiDebugService;

//    public ChatHandler(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients, OpenAiDebugService debugService) {
//        this.openAiDebugService = debugService;
//    }

    public ChatHandler(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
//        AsyncMcpToolCallbackProvider provider = new AsyncMcpToolCallbackProvider(mcpAsyncClients);
        SyncMcpToolCallbackProvider provider = new SyncMcpToolCallbackProvider(mcpSyncClients);

        ToolCallback[] tools = provider.getToolCallbacks();
        for (ToolCallback tool : tools) {
            System.out.println("=== TOOL INFO ===");
            System.out.println("Name: " + tool.getName());
            System.out.println("Description: " + tool.getDescription());
            System.out.println("Definition: " + tool.getToolDefinition());
            System.out.println("Metadata: " + tool.getToolMetadata());
        }

        this.chatClient = chatClientBuilder
                .defaultTools(provider)
                .build();

//        this.openAiDebugService = debugService;
    }

    public Mono<ServerResponse> askQuestion(ServerRequest request) {
        String userId = request.queryParam("userId").orElse("default");

        return request.bodyToMono(String.class)
                .flatMap(question -> {
                    logger.info("QUESTION: {}", question);

                    List<Message> history = chatHistories.computeIfAbsent(userId, k -> new ArrayList<>());
                    history.add(new UserMessage(question));

                    // Mistral 사용시 활성화
//                    ChatOptions options =  MistralAiChatOptions.builder()
//                            .temperature(0.7d)
//                            .toolChoice(MistralAiApi.ChatCompletionRequest.ToolChoice.ANY)
//                            .build();

                    //GPT-4o mini 사용시 사용
                    ChatOptions options = OpenAiChatOptions.builder()
                            .temperature(0.7d)
                            .toolChoice("auto")
                            .build();


                    Prompt prompt = new Prompt(history, options);


                    long startTime = System.currentTimeMillis();

                    return Mono.fromSupplier(() -> chatClient.prompt(prompt).call().chatResponse())
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(response -> {
                                logger.info("ASSISTANT response: {}", response);
                                history.add(response.getResult().getOutput());

                                long latency = System.currentTimeMillis() - startTime;

                                String outputText = response.getResult().getOutput().getText();
                                String model = response.getMetadata().getModel();

                                Integer promptTokens = null;
                                Integer completionTokens = null;
                                Integer totalTokens = null;
                                Integer tokensRemaining = null;

                                if (response.getMetadata().getUsage() != null) {
                                    promptTokens = response.getMetadata().getUsage().getPromptTokens();
                                    completionTokens = response.getMetadata().getUsage().getCompletionTokens();
                                    totalTokens = response.getMetadata().getUsage().getTotalTokens();
                                }
//                                tokensRemaining = response.getMetadata().getTokensRemaining();

                                Map<String, Object> tokenInfo = new LinkedHashMap<>();
                                tokenInfo.put("promptTokens(프롬프트에 사용된 토큰 수)", promptTokens);
                                tokenInfo.put("completionTokens(응답 생성에 사용된 토큰 수)", completionTokens);
                                tokenInfo.put("totalTokens(전체 요청에 사용된 토큰 수)", totalTokens);
                                tokenInfo.put("tokensRemaining(요청 후 남은 사용 가능 토큰 수)", tokensRemaining);

                                Map<String, Object> customResponse = new LinkedHashMap<>();
                                customResponse.put("response", outputText);
                                customResponse.put("model", model);
                                customResponse.put("timestamp", Instant.now().toString());
                                customResponse.put("userId", userId);
                                customResponse.put("latencyMs", latency);
                                customResponse.put("promptPreview", question.length() > 100 ? question.substring(0, 100) + "..." : question);
                                customResponse.put("version", "v1");
                                customResponse.putAll(tokenInfo);

                                return ServerResponse.ok().bodyValue(customResponse);
                            });
                });

    }
}
