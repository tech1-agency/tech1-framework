package tech1.framework.foundation.feigns.clients.openai.clients;

import tech1.framework.foundation.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import tech1.framework.foundation.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;

public interface OpenaiClient {
    OpenaiCompletionsResponse getCompletions(String apiKey, OpenaiCompletionsRequest request);
}
