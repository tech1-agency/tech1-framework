package io.tech1.framework.b2b.postgres.server.startup;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.b2b.postgres.server.domain.db.PostgresDbAnything;
import io.tech1.framework.b2b.postgres.server.repositories.PostgresAnythingRepository;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.tech1.framework.domain.constants.FrameworkConstants.VERSION_RUNTIME;
import static io.tech1.framework.domain.constants.LogsConstants.SERVER_STARTUP_LISTENER_1;
import static io.tech1.framework.domain.enums.Status.COMPLETED;
import static io.tech1.framework.domain.tests.constants.TestsConstants.TECH1;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartupEventListener {

    // Repositories
    private final PostgresInvitationCodesRepository postgresInvitationCodesRepository;
    private final PostgresUsersRepository postgresUsersRepository;
    private final PostgresUsersSessionsRepository postgresUsersSessionsRepository;
    private final PostgresAnythingRepository postgresAnythingRepository;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup() {
        try {
            var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
            LOGGER.info(SERVER_STARTUP_LISTENER_1, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);

            for (int i = 0; i < 15; i++) {
                var authorities = IntStream.range(0, 3).mapToObj(index -> new SimpleGrantedAuthority(randomString())).toList();
                var user = new PostgresDbUser(TECH1, randomPassword(), randomZoneId(), authorities);
                user.setAttributes(
                        Map.of(
                                "attr1", randomString(),
                                "attr2", randomLong()
                        )
                );
                user.setEmail(randomEmail());
                this.postgresUsersRepository.save(user);
            }
            for (int i = 0; i < 2; i++) {
                var session = new PostgresDbUserSession(
                        new JwtRefreshToken(randomStringLetterOrNumbersOnly(100)),
                        TECH1,
                        randomUserRequestMetadata()
                );
                this.postgresUsersSessionsRepository.save(session);
            }
            this.postgresAnythingRepository.save(new PostgresDbAnything(TECH1));
            var authorities = new ArrayList<>(List.of("admin")).stream().map(SimpleGrantedAuthority::new).toList();
            this.postgresInvitationCodesRepository.save(new PostgresDbInvitationCode(TECH1, authorities));

            LOGGER.warn("============================================================================================");
            LOGGER.warn("Users: " + this.postgresUsersRepository.count());
            LOGGER.warn("Sessions: " + this.postgresUsersSessionsRepository.count());
            LOGGER.warn("InvitationCodes: " + this.postgresUsersSessionsRepository.count());
            LOGGER.warn("Anything: " + this.postgresAnythingRepository.count());
            LOGGER.warn("============================================================================================");
        } catch (RuntimeException ex) {
            LOGGER.error("Startup exception", ex);
        }
    }
}
