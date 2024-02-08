package io.tech1.framework.b2b.mongodb.server.startup;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.b2b.base.security.jwt.startup.DefaultStartupEventListener;
import io.tech1.framework.domain.utilities.printer.PRINTER;
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

    @Autowired
    public StartupEventListener(
            AbstractEssenceConstructor essenceConstructor,
            EnvironmentUtility environmentUtility,
            IncidentPublisher incidentPublisher,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                essenceConstructor,
                environmentUtility,
                applicationFrameworkProperties
        );
        this.incidentPublisher = incidentPublisher;
    }

    @Override
    public void onStartup() {
        try {
            super.onStartup();
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            PRINTER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }
}
