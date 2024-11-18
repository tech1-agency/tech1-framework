package jbst.iam.crons;

import jbst.iam.services.BaseUsersSessionsService;
import jbst.iam.sessions.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.domain.properties.base.Cron;
import jbst.foundation.domain.properties.configs.SecurityJwtConfigs;
import jbst.foundation.domain.properties.configs.security.jwt.SessionConfigs;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static jbst.foundation.utilities.random.EntityUtility.set345;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SessionsCronTest {

    public static Stream<Arguments> cronArgs() {
        return Stream.of(
                Arguments.of(Cron.enabled()),
                Arguments.of(Cron.disabled())
        );
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        BaseUsersSessionsService baseUsersSessionsService() {
            return mock(BaseUsersSessionsService.class);
        }

        @Bean
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        SessionsCron sessionsCron() {
            return new SessionsCron(
                    this.sessionRegistry(),
                    this.baseUsersSessionsService(),
                    this.incidentPublisher(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final BaseUsersSessionsService baseUsersSessionsService;
    private final IncidentPublisher incidentPublisher;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final SessionsCron componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.baseUsersSessionsService,
                this.incidentPublisher,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.baseUsersSessionsService,
                this.incidentPublisher,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void processExceptionTest() {
        // Arrange
        var ex = new Exception();

        // Act
        this.componentUnderTest.processException(ex);

        // Assert
        verify(this.incidentPublisher).publishThrowable(ex);
    }

    @ParameterizedTest
    @MethodSource("cronArgs")
    void cleanByExpiredRefreshTokensTest(Cron cron) {
        // Arrange
        var usernames = set345(Username.class);
        if (cron.isEnabled()) {
            when(this.sessionRegistry.getActiveSessionsUsernames()).thenReturn(usernames);
        }
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(new SessionConfigs(cron, Cron.random())));

        // Act
        this.componentUnderTest.cleanByExpiredRefreshTokens();

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        if (cron.isEnabled()) {
            verify(this.sessionRegistry).getActiveSessionsUsernames();
            verify(this.sessionRegistry).cleanByExpiredRefreshTokens(usernames);
        }
    }

    @ParameterizedTest
    @MethodSource("cronArgs")
    void enableSessionsMetadataRenewTest(Cron cron) {
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(new SessionConfigs(Cron.random(), cron)));

        // Act
        this.componentUnderTest.enableSessionsMetadataRenew();

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        if (cron.isEnabled()) {
            verify(this.baseUsersSessionsService).enableUserRequestMetadataRenewCron();
        }
    }
}
