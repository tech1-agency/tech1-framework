package io.tech1.framework.b2b.base.security.jwt.assistants.current.base;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityPrincipalUtils;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringWidget;
import io.tech1.framework.domain.properties.configs.HardwareMonitoringConfigs;
import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import io.tech1.framework.domain.tuples.TuplePresence;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
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
        UsersSessionsRepository usersSessionsRepository() {
            return mock(UsersSessionsRepository.class);
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
        SecurityPrincipalUtils securityPrincipalUtility() {
            return mock(SecurityPrincipalUtils.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        CurrentSessionAssistant currentSessionAssistant() {
            return new BaseCurrentSessionAssistant(
                    this.sessionRegistry(),
                    this.usersSessionsRepository(),
                    this.hardwareMonitoringStore(),
                    this.cookieProvider(),
                    this.securityPrincipalUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final UsersSessionsRepository usersSessionsRepository;
    private final HardwareMonitoringStore hardwareMonitoringStore;
    private final CookieProvider cookieProvider;
    private final SecurityPrincipalUtils securityPrincipalUtils;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final CurrentSessionAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.usersSessionsRepository,
                this.hardwareMonitoringStore,
                this.cookieProvider,
                this.securityPrincipalUtils,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.usersSessionsRepository,
                this.hardwareMonitoringStore,
                this.cookieProvider,
                this.securityPrincipalUtils,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void getCurrentUsernameTest() {
        // Arrange
        var expectedJwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtils.getAuthenticatedUsername()).thenReturn(expectedJwtUser.getUsername());

        // Act
        var actualUsername = this.componentUnderTest.getCurrentUsername();

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedUsername();
        assertThat(actualUsername).isEqualTo(expectedJwtUser.username());
    }

    @Test
    void getCurrentJwtUserTest() {
        // Arrange
        var expectedJwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtils.getAuthenticatedJwtUser()).thenReturn(expectedJwtUser);

        // Act
        var actualJwtUser = this.componentUnderTest.getCurrentJwtUser();

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedJwtUser();
        assertThat(actualJwtUser).isEqualTo(expectedJwtUser);
    }

    @Test
    void getCurrentClientUserTest() {
        // Arrange
        var user = new JwtUser(
                UserId.random(),
                Username.random(),
                Password.random(),
                randomZoneId(),
                new HashSet<>(),
                Email.random(),
                randomString(),
                new HashMap<>()
        );
        when(this.securityPrincipalUtils.getAuthenticatedJwtUser()).thenReturn(user);
        var hardwareMonitoringWidget = entity(HardwareMonitoringWidget.class);
        when(this.hardwareMonitoringStore.getHardwareMonitoringWidget()).thenReturn(hardwareMonitoringWidget);
        when(this.applicationFrameworkProperties.getHardwareMonitoringConfigs()).thenReturn(TestsPropertiesConstants.HARDWARE_MONITORING_CONFIGS);

        // Act
        var currentClientUser = this.componentUnderTest.getCurrentClientUser();

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedJwtUser();
        verify(this.hardwareMonitoringStore).getHardwareMonitoringWidget();
        verify(this.applicationFrameworkProperties).getHardwareMonitoringConfigs();
        assertThat(currentClientUser.getUsername()).isEqualTo(Username.of(user.getUsername()));
        assertThat(currentClientUser.getEmail()).isEqualTo(user.email());
        assertThat(currentClientUser.getName()).isEqualTo(user.name());
        assertThat(currentClientUser.getAttributes()).isNotNull();
        assertThat(currentClientUser.getAttributes()).hasSize(1);
        assertThat(currentClientUser.getAttributes()).containsOnlyKeys("hardware");
    }

    @Test
    void getCurrentClientUserNoAttributesNoHardwareTest() {
        // Arrange
        var user = entity(JwtUser.class);
        when(this.securityPrincipalUtils.getAuthenticatedJwtUser()).thenReturn(user);
        var hardwareMonitoringWidget = entity(HardwareMonitoringWidget.class);
        when(this.hardwareMonitoringStore.getHardwareMonitoringWidget()).thenReturn(hardwareMonitoringWidget);
        when(this.applicationFrameworkProperties.getHardwareMonitoringConfigs()).thenReturn(HardwareMonitoringConfigs.disabled());

        // Act
        var currentClientUser = this.componentUnderTest.getCurrentClientUser();

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedJwtUser();
        verify(this.applicationFrameworkProperties).getHardwareMonitoringConfigs();
        assertThat(currentClientUser.getUsername()).isEqualTo(Username.of(user.getUsername()));
        assertThat(currentClientUser.getEmail()).isEqualTo(user.email());
        assertThat(currentClientUser.getName()).isEqualTo(user.name());
        assertThat(currentClientUser.getAttributes()).isNotNull();
        assertThat(currentClientUser.getAttributes()).isEmpty();
    }

    @Test
    void getCurrentUserSessionTest() throws AccessTokenNotFoundException {
        // Arrange
        var session = entity(UserSession.class);
        var request = mock(HttpServletRequest.class);
        var cookie = CookieAccessToken.random();
        var accessToken = JwtAccessToken.of(cookie.value());
        when(this.cookieProvider.readJwtAccessToken(request)).thenReturn(cookie);
        when(this.usersSessionsRepository.isPresent(accessToken)).thenReturn(TuplePresence.present(session));

        // Act
        var actual = this.componentUnderTest.getCurrentUserSession(request);

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(request);
        verify(this.usersSessionsRepository).isPresent(accessToken);
        assertThat(actual).isEqualTo(session);
    }

    @Test
    void getCurrentUserDbSessionsTableTest() {
        // Arrange
        var username = Username.random();
        var cookie = CookieAccessToken.random();
        var sessionsTable = entity(ResponseUserSessionsTable.class);
        when(this.securityPrincipalUtils.getAuthenticatedUsername()).thenReturn(username.identifier());
        when(this.sessionRegistry.getSessionsTable(username, cookie)).thenReturn(sessionsTable);

        // Act
        var actual = this.componentUnderTest.getCurrentUserDbSessionsTable(cookie);

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedUsername();
        verify(this.sessionRegistry).cleanByExpiredRefreshTokens(Set.of(username));
        verify(this.sessionRegistry).getSessionsTable(username, cookie);
        assertThat(actual).isEqualTo(sessionsTable);
    }
}
