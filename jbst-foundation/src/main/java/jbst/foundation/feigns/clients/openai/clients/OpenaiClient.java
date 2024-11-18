package jbst.foundation.feigns.clients.openai.clients;

import jbst.foundation.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import jbst.foundation.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;

public interface OpenaiClient {
    OpenaiCompletionsResponse getCompletions(String apiKey, OpenaiCompletionsRequest request);
}
