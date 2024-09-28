package tech1.framework.foundation.feigns.clients.openai.domain.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenaiCompletionsRequest(
        String model,
        String prompt,
        @JsonProperty("max_tokens") int maxTokens,
        double temperature
) {

    public static OpenaiCompletionsRequest davinci003(String prompt) {
        return new OpenaiCompletionsRequest(
                "text-davinci-003",
                prompt,
                4000,
                1.0d
        );
    }
}
