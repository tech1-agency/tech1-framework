package io.tech1.framework.feigns.clients.openai.clients;

import io.tech1.framework.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import io.tech1.framework.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;

public interface OpenaiClient {
    OpenaiCompletionsResponse getCompletions(String apiKey, OpenaiCompletionsRequest request);
}
