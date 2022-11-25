package io.tech1.framework.b2b.mongodb.security.jwt.websockets.template;

import io.tech1.framework.b2b.mongodb.security.jwt.websockets.domain.events.WebsocketEvent;
import io.tech1.framework.domain.base.Username;
import org.springframework.scheduling.annotation.Async;

public interface WssMessagingTemplate {
    @Async
    void sendEventToUser(Username username, String destination, WebsocketEvent event);
}
