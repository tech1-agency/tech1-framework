package io.tech1.framework.b2b.mongodb.server.startup;

import io.tech1.framework.b2b.mongodb.security.jwt.essence.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.startup.DefaultStartupEventListener;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkConstants.VERSION_RUNTIME;
import static io.tech1.framework.domain.constants.LogsConstants.SERVER_STARTUP_LISTENER_1;
import static io.tech1.framework.domain.enums.Status.COMPLETED;

@Slf4j
@Service
public class StartupEventListener extends DefaultStartupEventListener {

    // Publishers
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Autowired
    public StartupEventListener(
            EssenceConstructor essenceConstructor,
            EnvironmentUtility environmentUtility,
            IncidentPublisher incidentPublisher,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(essenceConstructor, environmentUtility);
        this.incidentPublisher = incidentPublisher;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
    }

    @Override
    public void onStartup() {
        try {
            super.onStartup();
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            LOGGER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }
}
