package jbst.foundation.feigns.clients.telegram.clients.impl;

import feign.RetryableException;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.feigns.clients.telegram.clients.TelegramClient;
import jbst.foundation.feigns.clients.telegram.definitions.TelegramDefinition;
import jbst.foundation.feigns.clients.telegram.domain.requests.TelegramMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TelegramClientImpl implements TelegramClient {

    // Definitions
    private final TelegramDefinition definition;

    @Override
    public void sendMessage(TelegramMessageRequest message) {
        try {
            this.definition.sendMessage(
                    message.token(),
                    message.getReqBody()
            );
        } catch (RetryableException ex) {
            LOGGER.warn(JbstConstants.Logs.SERVER_OFFLINE, "Telegram", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
