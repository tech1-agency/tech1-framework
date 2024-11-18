package tech1.framework.foundation.feigns.clients.openai.definions;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import tech1.framework.foundation.feigns.clients.openai.domain.requests.OpenaiCompletionsRequest;
import tech1.framework.foundation.feigns.clients.openai.domain.responses.OpenaiCompletionsResponse;

public interface OpenaiDefinition {
    @RequestLine("POST /v1/completions")
    @Headers(
            {
                    "Authorization: Bearer {token}",
                    "Content-Type: application/json"
            }
    )
    OpenaiCompletionsResponse completions(
            @Param("token") String token,
            OpenaiCompletionsRequest request
    );
}
