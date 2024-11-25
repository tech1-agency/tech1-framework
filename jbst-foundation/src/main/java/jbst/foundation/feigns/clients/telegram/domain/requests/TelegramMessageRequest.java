package jbst.foundation.feigns.clients.telegram.domain.requests;

import java.util.Map;

public record TelegramMessageRequest(
        String token,
        String chatId,
        String text
) {

    public Map<String, Object> getReqBody() {
        return Map.of(
                "chat_id", this.chatId,
                "text", this.text,
                "parse_mode", "HTML"
        );
    }
}
