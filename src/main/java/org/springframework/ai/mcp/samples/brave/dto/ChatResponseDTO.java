package org.springframework.ai.mcp.samples.brave.dto;

import lombok.Data;

@Data
public class ChatResponseDTO {

    private String response;
    private String model;
    private String timestamp;
    private String userId;
    private Integer tokensUsed;
    private Long latencyMs;
    private String promptPreview;
    private String version;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer tokensRemaining;


    public ChatResponseDTO() {}

    public ChatResponseDTO(String response, String model, String timestamp, String userId, Integer tokensUsed, Long latencyMs, String promptPreview, String version, Integer promptTokens, Integer completionTokens, Integer tokensRemaining) {
        this.response = response;
        this.model = model;
        this.timestamp = timestamp;
        this.userId = userId;
        this.tokensUsed = tokensUsed;
        this.latencyMs = latencyMs;
        this.promptPreview = promptPreview;
        this.version = version;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.tokensRemaining = tokensRemaining;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getPromptPreview() {
        return promptPreview;
    }

    public void setPromptPreview(String promptPreview) {
        this.promptPreview = promptPreview;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getTokensRemaining() {
        return tokensRemaining;
    }

    public void setTokensRemaining(Integer tokensRemaining) {
        this.tokensRemaining = tokensRemaining;
    }
}
