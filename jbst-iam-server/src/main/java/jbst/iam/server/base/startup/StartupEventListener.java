package jbst.iam.server.base.startup;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import tech1.framework.iam.essence.AbstractEssenceConstructor;
import tech1.framework.iam.startup.DefaultStartupEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static tech1.framework.foundation.domain.constants.LogsConstants.SERVER_STARTUP_LISTENER_1;
import static tech1.framework.foundation.domain.enums.Status.COMPLETED;

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
