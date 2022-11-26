package io.tech1.framework.b2b.mongodb.server.startup;

import io.tech1.framework.b2b.mongodb.security.jwt.essence.EssenceConstructor;
import io.tech1.framework.b2b.mongodb.security.jwt.startup.DefaultStartupEventListener;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.b2b.mongodb.server.constants.ApplicationConstants.SERVER_NAME;
import static io.tech1.framework.domain.constants.FrameworkConstants.VERSION_RUNTIME;
import static io.tech1.framework.domain.constants.LogsConstants.SERVER_STARTUP_LISTENER;
import static io.tech1.framework.domain.enums.Status.COMPLETED;

@Slf4j
@Service
public class StartupEventListener extends DefaultStartupEventListener {

    @Autowired
    public StartupEventListener(
            EssenceConstructor essenceConstructor,
            EnvironmentUtility environmentUtility
    ) {
        super(essenceConstructor, environmentUtility);
    }

    @Override
    public void onStartup() {
        super.onStartup();
        LOGGER.info(SERVER_STARTUP_LISTENER, SERVER_NAME, VERSION_RUNTIME, COMPLETED);
    }
}
