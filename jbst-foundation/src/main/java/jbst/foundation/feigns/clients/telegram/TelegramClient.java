package jbst.foundation.feigns.clients.telegram;

import feign.RetryableException;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.feigns.clients.telegram.domain.requests.TelegramMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TelegramClient {

    // Definitions
    private final TelegramDefinition definition;

    public final void sendMessage(TelegramMessageRequest request) {
        try {
            this.definition.sendMessage(
                    request.token(),
                    request.getRequestBody()
            );
        } catch (RetryableException ex) {
            LOGGER.warn(JbstConstants.Logs.SERVER_OFFLINE, "Telegram", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
