package jbst.foundation.feigns.openai;

import jbst.foundation.feigns.openai.domain.requests.OpenaiCompletionsRequest;
import jbst.foundation.feigns.openai.domain.responses.OpenaiCompletionsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OpenaiClient {

    // Definitions
    private final OpenaiDefinition definition;

    public final OpenaiCompletionsResponse getCompletions(String apiKey, OpenaiCompletionsRequest request) {
        return this.definition.completions(
                apiKey,
                request
        );
    }
}
