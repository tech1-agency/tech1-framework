package io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.base;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityPrincipalUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringWidget;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.configs.HardwareMonitoringConfigs;
import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.tech1.framework.domain.http.requests.UserRequestMetadata.processed;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseCurrentSessionAssistantTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        UserSessionService userSessionService() {
            return mock(UserSessionService.class);
        }

        @Bean
        HardwareMonitoringStore hardwareMonitoringStore() {
            return mock(HardwareMonitoringStore.class);
        }

        @Bean
        CookieProvider cookieProvider() {
            return mock(CookieProvider.class);
        }

        @Bean
        SecurityPrincipalUtility securityPrincipalUtility() {
            return mock(SecurityPrincipalUtility.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        CurrentSessionAssistant currentSessionAssistant() {
            return new BaseCurrentSessionAssistant(
                    this.sessionRegistry(),
                    this.userSessionService(),
                    this.hardwareMonitoringStore(),
                    this.cookieProvider(),
                    this.securityPrincipalUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final UserSessionService userSessionService;
    private final HardwareMonitoringStore hardwareMonitoringStore;
    private final CookieProvider cookieProvider;
    private final SecurityPrincipalUtility securityPrincipalUtility;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final CurrentSessionAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.hardwareMonitoringStore,
                this.userSessionService,
                this.cookieProvider,
                this.securityPrincipalUtility,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.hardwareMonitoringStore,
                this.userSessionService,
                this.cookieProvider,
                this.securityPrincipalUtility,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void getCurrentUsernameTest() {
        // Arrange
        var expectedJwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedUsername()).thenReturn(expectedJwtUser.getUsername());

        // Act
        var actualUsername = this.componentUnderTest.getCurrentUsername();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedUsername();
        assertThat(actualUsername).isEqualTo(expectedJwtUser.getDbUser().getUsername());
    }

    @Test
    void getCurrentUserIdTest() {
        // Arrange
        var expectedJwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(expectedJwtUser);

        // Act
        var actualUserId = this.componentUnderTest.getCurrentUserId();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
        assertThat(actualUserId).isEqualTo(expectedJwtUser.getDbUser().getId());
    }

    @Test
    void getCurrentDbUserTest() {
        // Arrange
        var jwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(jwtUser);

        // Act
        var currentDbUser = this.componentUnderTest.getCurrentDbUser();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
        assertThat(currentDbUser).isEqualTo(jwtUser.getDbUser());
    }

    @Test
    void getCurrentJwtUserTest() {
        // Arrange
        var expectedJwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(expectedJwtUser);

        // Act
        var actualJwtUser = this.componentUnderTest.getCurrentJwtUser();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
        assertThat(actualJwtUser).isEqualTo(expectedJwtUser);
    }

    @Test
    void getCurrentClientUserTest() {
        // Arrange
        var jwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(jwtUser);
        var hardwareMonitoringWidget = entity(HardwareMonitoringWidget.class);
        when(this.hardwareMonitoringStore.getHardwareMonitoringWidget()).thenReturn(hardwareMonitoringWidget);
        when(this.applicationFrameworkProperties.getHardwareMonitoringConfigs()).thenReturn(TestsPropertiesConstants.HARDWARE_MONITORING_CONFIGS);

        // Act
        var currentClientUser = this.componentUnderTest.getCurrentClientUser();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
        verify(this.hardwareMonitoringStore).getHardwareMonitoringWidget();
        verify(this.applicationFrameworkProperties).getHardwareMonitoringConfigs();
        assertThat(currentClientUser.getUsername()).isEqualTo(Username.of(jwtUser.getUsername()));
        assertThat(currentClientUser.getName()).isEqualTo(jwtUser.getDbUser().getName());
        assertThat(currentClientUser.getEmail()).isEqualTo(jwtUser.getDbUser().getEmail());
        assertThat(currentClientUser.getAttributes()).isNotNull();
        assertThat(currentClientUser.getAttributes()).hasSize(1);
        assertThat(currentClientUser.getAttributes()).containsOnlyKeys("hardware");
    }

    @Test
    void getCurrentClientUserNoHardwareTest() {
        // Arrange
        var jwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(jwtUser);
        var hardwareMonitoringWidget = entity(HardwareMonitoringWidget.class);
        when(this.hardwareMonitoringStore.getHardwareMonitoringWidget()).thenReturn(hardwareMonitoringWidget);
        when(this.applicationFrameworkProperties.getHardwareMonitoringConfigs()).thenReturn(HardwareMonitoringConfigs.disabled());

        // Act
        var currentClientUser = this.componentUnderTest.getCurrentClientUser();

        // Assert
        verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
        verify(this.applicationFrameworkProperties).getHardwareMonitoringConfigs();
        assertThat(currentClientUser.getUsername()).isEqualTo(Username.of(jwtUser.getUsername()));
        assertThat(currentClientUser.getName()).isEqualTo(jwtUser.getDbUser().getName());
        assertThat(currentClientUser.getEmail()).isEqualTo(jwtUser.getDbUser().getEmail());
        assertThat(currentClientUser.getAttributes()).isNotNull();
        assertThat(currentClientUser.getAttributes()).isEmpty();
    }

    @Test
    void getCurrentUserDbSessionsTableTest() {
        // Arrange
        var jwtUser = entity(JwtUser.class);
        var username = jwtUser.getDbUser().getUsername();
        var validJwtRefreshToken = randomString();
        var cookieRefreshToken = new CookieRefreshToken(validJwtRefreshToken);

        Function<Tuple2<UserRequestMetadata, String>, DbUserSession> sessionFnc =
                    tuple2 -> new DbUserSession(new JwtRefreshToken(tuple2.b()), randomUsername(), tuple2.a());

        var validSession = sessionFnc.apply(new Tuple2<>(processed(validGeoLocation(), validUserAgentDetails()), validJwtRefreshToken));
        var invalidSession1 = sessionFnc.apply(new Tuple2<>(processed(invalidGeoLocation(), validUserAgentDetails()), randomString()));
        var invalidSession2 = sessionFnc.apply(new Tuple2<>(processed(validGeoLocation(), invalidUserAgentDetails()), randomString()));
        var invalidSession3 = sessionFnc.apply(new Tuple2<>(processed(invalidGeoLocation(), invalidUserAgentDetails()), randomString()));

        // userSessions, expectedSessionSize, expectedAnyProblems
        List<Tuple3<List<DbUserSession>, Integer, Boolean>> cases = new ArrayList<>();
        cases.add(
                new Tuple3<>(
                        List.of(validSession),
                        1,
                        false
                )
        );
        cases.add(
                new Tuple3<>(
                        List.of(validSession, invalidSession1),
                        2,
                        true
                )
        );
        cases.add(
                new Tuple3<>(
                        List.of(validSession, invalidSession1, invalidSession2),
                        3,
                        true
                )
        );
        cases.add(
                new Tuple3<>(
                        List.of(validSession, invalidSession1, invalidSession2, invalidSession3),
                        4,
                        true
                )
        );

        // Act
        cases.forEach(item -> {
            try {
                // Arrange
                var httpServletRequest = mock(HttpServletRequest.class);
                when(this.securityPrincipalUtility.getAuthenticatedJwtUser()).thenReturn(jwtUser);
                var userSessions = item.a();
                var expectedSessionSize = item.b();
                var expectedAnyProblems = item.c();
                when(this.userSessionService.findByUsername(username)).thenReturn(userSessions);
                when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);

                // Act
                var currentUserDbSessionsTable = this.componentUnderTest.getCurrentUserDbSessionsTable(httpServletRequest);

                // Assert
                verify(this.securityPrincipalUtility).getAuthenticatedJwtUser();
                verify(this.userSessionService, times(2)).findByUsername(username);
                verify(this.sessionRegistry).cleanByExpiredRefreshTokens(userSessions);
                verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
                assertThat(currentUserDbSessionsTable).isNotNull();
                assertThat(currentUserDbSessionsTable.getSessions()).hasSize(expectedSessionSize);
                assertThat(currentUserDbSessionsTable.getSessions().stream().filter(ResponseUserSession2::isCurrent).count()).isEqualTo(1);
                assertThat(currentUserDbSessionsTable.getSessions().stream().filter(session -> "Current session".equals(session.getActivity())).count()).isEqualTo(1);
                assertThat(currentUserDbSessionsTable.isAnyProblem()).isEqualTo(expectedAnyProblems);

                reset(
                        this.sessionRegistry,
                        this.userSessionService,
                        this.cookieProvider,
                        this.securityPrincipalUtility
                );
            } catch (CookieRefreshTokenNotFoundException ex) {
                // ignored
            }
        });
    }
}
