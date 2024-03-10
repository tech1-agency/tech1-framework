package io.tech1.framework.feigns.clients.openai.domain.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenaiCompletionsUsageResponse(
        @JsonProperty("prompt_tokens")
        int promptTokens,
        @JsonProperty("completion_tokens")
        int completionTokens,
        @JsonProperty("total_tokens")
        int totalTokens
) {
}
