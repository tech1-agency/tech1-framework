package jbst.foundation.feigns.openai.domain.responses;

import java.util.List;

public record OpenaiCompletionsResponse(
        String id,
        String object,
        long created,
        String model,
        List<OpenaiCompletionsChoiceResponse> choices,
        OpenaiCompletionsUsageResponse usage
) {
}
