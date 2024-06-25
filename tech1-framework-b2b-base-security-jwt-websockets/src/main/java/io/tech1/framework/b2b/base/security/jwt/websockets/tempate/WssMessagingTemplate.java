package io.tech1.framework.b2b.base.security.jwt.websockets.tempate;

import io.tech1.framework.b2b.base.security.jwt.websockets.domain.events.WebsocketEvent;
import io.tech1.framework.foundation.domain.base.Username;
import org.springframework.scheduling.annotation.Async;

public interface WssMessagingTemplate {
    @Async
    void sendEventToUser(Username username, String destination, WebsocketEvent event);
}
