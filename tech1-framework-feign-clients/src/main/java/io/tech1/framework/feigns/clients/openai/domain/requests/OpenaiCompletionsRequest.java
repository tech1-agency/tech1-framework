package io.tech1.framework.feigns.clients.openai.domain.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// Lombok
@Data
public class OpenaiCompletionsRequest {
    private final String model;

    private final String prompt;

    @JsonProperty("max_tokens")
    private final int maxTokens;

    private final double temperature;

    public OpenaiCompletionsRequest(String prompt) {
        this.model = "text-davinci-003";
        this.prompt = prompt;
        this.maxTokens = 4000;
        this.temperature = 1.0;
    }
}
