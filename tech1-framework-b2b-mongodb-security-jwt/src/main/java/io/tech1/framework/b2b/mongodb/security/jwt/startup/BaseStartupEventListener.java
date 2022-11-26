package io.tech1.framework.b2b.mongodb.security.jwt.startup;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

public interface BaseStartupEventListener {
    @EventListener(ApplicationStartedEvent.class)
    void onStartup();
}
