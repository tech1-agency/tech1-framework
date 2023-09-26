package io.tech1.framework.b2b.base.security.jwt.crons;

import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.Cron;
import io.tech1.framework.domain.properties.configs.SecurityJwtConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.SessionConfigs;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.EntityUtility.set345;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SessionsCronTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        SessionsCron sessionsCron() {
            return new SessionsCron(
                    this.sessionRegistry(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final SessionsCron componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry
        );
    }

    @Test
    void cleanByExpiredRefreshTokensDisabled() {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(new SessionConfigs(Cron.disabled(), Cron.random())));

        // Act
        this.componentUnderTest.cleanByExpiredRefreshTokens();

        // Assert
        verifyNoMoreInteractions(this.sessionRegistry);
    }

    @Test
    void cleanByExpiredRefreshTokensEnabled() {
        // Arrange
        var usernames = set345(Username.class);
        when(this.sessionRegistry.getActiveSessionsUsernames()).thenReturn(usernames);
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(new SessionConfigs(Cron.enabled(), Cron.random())));

        // Act
        this.componentUnderTest.cleanByExpiredRefreshTokens();

        // Assert
        verify(this.sessionRegistry).getActiveSessionsUsernames();
        verify(this.sessionRegistry).cleanByExpiredRefreshTokens(usernames);
    }
}
