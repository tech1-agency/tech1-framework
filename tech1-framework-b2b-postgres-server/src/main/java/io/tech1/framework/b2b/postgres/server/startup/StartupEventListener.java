package io.tech1.framework.b2b.postgres.server.startup;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUserRepository;
import io.tech1.framework.b2b.postgres.server.domain.db.PostgresDbAnything;
import io.tech1.framework.b2b.postgres.server.repositories.PostgresAnythingRepository;
import io.tech1.framework.domain.base.Username;
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

    // Repositories
    private final PostgresUserRepository postgresUserRepository;
    private final PostgresAnythingRepository postgresAnythingRepository;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup() {
        try {
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            LOGGER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);

            var username = Username.of("tech1");
            this.postgresUserRepository.save(new PostgresDbUser(username));
            this.postgresAnythingRepository.save(new PostgresDbAnything(username));

            LOGGER.warn("============================================================================================");
            LOGGER.warn("Users: " + this.postgresUserRepository.count());
            LOGGER.warn("Anything: " + this.postgresAnythingRepository.count());
            LOGGER.warn("============================================================================================");
        } catch (RuntimeException ex) {
            LOGGER.error("Startup exception", ex);
        }
    }
}
