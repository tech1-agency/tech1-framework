package jbst.foundation.feigns.clients.telegram.clients;

import jbst.foundation.feigns.clients.telegram.domain.requests.TelegramMessageRequest;

public interface TelegramClient {
    void sendMessage(TelegramMessageRequest message);
}
