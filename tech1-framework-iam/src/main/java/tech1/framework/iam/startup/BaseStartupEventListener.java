package tech1.framework.iam.startup;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

public interface BaseStartupEventListener {
    @EventListener(ApplicationStartedEvent.class)
    void onStartup();
}
