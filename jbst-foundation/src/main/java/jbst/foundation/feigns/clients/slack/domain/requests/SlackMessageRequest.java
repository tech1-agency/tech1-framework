package jbst.foundation.feigns.clients.slack.domain.requests;

import java.util.Map;

public record SlackMessageRequest(
        String token,
        String channel,
        String text
) {

    public Map<String, Object> getRequestBody() {
        return Map.of(
                "channel", this.channel,
                "text", this.text
        );
    }
}
