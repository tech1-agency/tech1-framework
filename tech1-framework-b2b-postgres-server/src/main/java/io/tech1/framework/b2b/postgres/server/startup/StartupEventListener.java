package io.tech1.framework.b2b.postgres.server.startup;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.b2b.base.security.jwt.startup.DefaultStartupEventListener;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            IncidentPublisher incidentPublisher,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                essenceConstructor,
                applicationFrameworkProperties
        );
        this.incidentPublisher = incidentPublisher;
    }

    @Override
    public void onStartup() {
        try {
            super.onStartup();
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            var mavenDetails = this.applicationFrameworkProperties.getMavenConfigs().asMavenDetails();
            LOGGER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), mavenDetails.version(), COMPLETED);
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }
}
