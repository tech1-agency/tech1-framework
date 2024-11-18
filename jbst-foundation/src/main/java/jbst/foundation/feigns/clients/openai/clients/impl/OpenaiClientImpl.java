package jbst.foundation.feigns.clients.openai.clients.impl;

import jbst.foundation.feigns.clients.openai.clients.OpenaiClient;
import jbst.foundation.feigns.clients.openai.definions.OpenaiDefinition;
import jbst.foundation.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import jbst.foundation.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;
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
