package io.tech1.framework.b2b.postgres.server.startup;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkConstants.VERSION_RUNTIME;
import static io.tech1.framework.domain.constants.LogsConstants.SERVER_STARTUP_LISTENER_1;
import static io.tech1.framework.domain.enums.Status.COMPLETED;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartupEventListener {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup() {
        try {
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            LOGGER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);
        } catch (RuntimeException ex) {
            // incidents
        }
    }
}
