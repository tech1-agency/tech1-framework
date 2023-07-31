package io.tech1.framework.b2b.base.security.jwt.assistants.current.base;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityPrincipalUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringWidget;
import io.tech1.framework.domain.properties.configs.HardwareMonitoringConfigs;
import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
        HardwareMonitoringStore hardwareMonitoringStore() {
            return mock(HardwareMonitoringStore.class);
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
                    this.hardwareMonitoringStore(),
                    this.securityPrincipalUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final HardwareMonitoringStore hardwareMonitoringStore;
    private final SecurityPrincipalUtils securityPrincipalUtils;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final CurrentSessionAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.hardwareMonitoringStore,
                this.securityPrincipalUtils,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.hardwareMonitoringStore,
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
                entity(UserId.class),
                randomUsername(),
                randomPassword(),
                randomZoneId(),
                new ArrayList<>(),
                randomEmail(),
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
        var jwtUser = entity(JwtUser.class);
        when(this.securityPrincipalUtils.getAuthenticatedJwtUser()).thenReturn(jwtUser);
        var hardwareMonitoringWidget = entity(HardwareMonitoringWidget.class);
        when(this.hardwareMonitoringStore.getHardwareMonitoringWidget()).thenReturn(hardwareMonitoringWidget);
        when(this.applicationFrameworkProperties.getHardwareMonitoringConfigs()).thenReturn(HardwareMonitoringConfigs.disabled());

        // Act
        var currentClientUser = this.componentUnderTest.getCurrentClientUser();

        // Assert
        verify(this.securityPrincipalUtils).getAuthenticatedJwtUser();
        verify(this.applicationFrameworkProperties).getHardwareMonitoringConfigs();
        assertThat(currentClientUser.getUsername()).isEqualTo(Username.of(jwtUser.getUsername()));
        assertThat(currentClientUser.getEmail()).isEqualTo(jwtUser.email());
        assertThat(currentClientUser.getName()).isEqualTo(jwtUser.name());
        assertThat(currentClientUser.getAttributes()).isNotNull();
        assertThat(currentClientUser.getAttributes()).isEmpty();
    }

    @Test
    void getCurrentUserDbSessionsTableTest() {
        // Arrange
        var username = entity(Username.class);
        var cookie = entity(CookieRefreshToken.class);
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
