package tech1.framework.foundation.feigns.clients.openai.clients.impl;

import tech1.framework.foundation.feigns.clients.openai.clients.OpenaiClient;
import tech1.framework.foundation.feigns.clients.openai.definions.OpenaiDefinition;
import tech1.framework.foundation.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import tech1.framework.foundation.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OpenaiClientImpl implements OpenaiClient {

    // Definitions
    private final OpenaiDefinition openaiDefinition;

    @Override
    public OpenaiCompletionsResponse getCompletions(String apiKey, OpenaiCompletionsRequest request) {
        return this.openaiDefinition.completions(
                apiKey,
                request
        );
    }
}
