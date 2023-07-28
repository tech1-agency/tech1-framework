package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.UserEmailUtility;
import io.tech1.framework.domain.utilities.time.LocalDateTimeUtility;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.ZoneOffset;

import static io.tech1.framework.domain.constants.DatetimeConstants.DTF1;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.getTimestamp;
import static io.tech1.framework.domain.utilities.time.LocalDateUtility.now;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserEmailUtilityImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ResourceLoader resourceLoader;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        EnvironmentUtility environmentUtility() {
            return mock(EnvironmentUtility.class);
        }

        @Bean
        UserEmailUtility userEmailUtility() {
            return new UserEmailUtilityImpl(
                    this.resourceLoader,
                    this.environmentUtility(),
                    this.applicationFrameworkProperties
            );
        }
    }

    // Utilities
    private final EnvironmentUtility environmentUtility;

    private final UserEmailUtility componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.environmentUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.environmentUtility
        );
    }

    @RepeatedTest(5)
    void getSubjectTest() {
        // Arrange
        when(this.environmentUtility.getActiveProfile()).thenReturn("stage");

        // Act
        var subject = this.componentUnderTest.getSubject("Authentication Login");

        // Assert
        verify(this.environmentUtility).getActiveProfile();
        assertThat(subject)
                .startsWith("[Tech1] Authentication Login on [tech1-spring-boot-server@stage] — ")
                .endsWith(" (UTC)");
        subject = subject.replace("[Tech1] Authentication Login on [tech1-spring-boot-server@stage] — ", "");
        subject = subject.replace(" (UTC)", "");
        var timestamp = getTimestamp(LocalDateTimeUtility.parse(subject, DTF1), ZoneOffset.UTC);
        assertThat(getCurrentTimestamp() - timestamp).isBetween(0L, 2000L);
    }

    @Test
    void getAuthenticationLoginTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getAuthenticationLoginTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("framework-account-accessed");
    }

    @Test
    void getSessionRefreshedTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getSessionRefreshedTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("framework-account-accessed");
    }

    @Test
    void getAuthenticationLoginOrSessionRefreshedVariablesTest() {
        // Arrange
        var username = randomUsername();
        var userRequestMetadata = validUserRequestMetadata();
        var accountAccessMethod = randomEnum(AccountAccessMethod.class);

        // Act
        var variables = this.componentUnderTest.getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                accountAccessMethod
        );

        // Assert
        assertThat(variables)
                .hasSize(7)
                .containsEntry("year", now(UTC).getYear())
                .containsEntry("username", username.identifier())
                .containsEntry("accessMethod", accountAccessMethod.getValue())
                .containsEntry("where", "🇺🇦 Ukraine, Lviv")
                .containsEntry("what", "Chrome, macOS on Desktop")
                .containsEntry("ipAddress", "127.0.0.1")
                .containsEntry("webclientURL", "http://127.0.0.1:3000");
    }
}
