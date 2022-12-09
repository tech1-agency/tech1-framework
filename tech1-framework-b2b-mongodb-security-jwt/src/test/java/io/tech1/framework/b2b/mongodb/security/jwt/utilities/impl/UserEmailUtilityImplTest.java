package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.enums.AccountAccessType;
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
import java.time.format.DateTimeFormatter;

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
public class UserEmailUtilityImplTest {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

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
    public void beforeEach() {
        reset(
                this.environmentUtility
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.environmentUtility
        );
    }

    @RepeatedTest(5)
    public void getSubjectTest() {
        // Arrange
        when(this.environmentUtility.getActiveProfile()).thenReturn("stage");

        // Act
        var subject = this.componentUnderTest.getSubject("Authentication Login");

        // Assert
        verify(this.environmentUtility).getActiveProfile();
        assertThat(subject).startsWith("[Tech1] Authentication Login on [tech1-spring-boot-server@stage] — ");
        assertThat(subject).endsWith(" (UTC)");
        subject = subject.replace("[Tech1] Authentication Login on [tech1-spring-boot-server@stage] — ", "");
        subject = subject.replace(" (UTC)", "");
        var timestamp = getTimestamp(LocalDateTimeUtility.parse(subject, DTF), ZoneOffset.UTC);
        assertThat(getCurrentTimestamp() - timestamp).isBetween(0L, 2000L);
    }

    @Test
    public void getAuthenticationLoginTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getAuthenticationLoginTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("framework-account-accessed");
    }

    @Test
    public void getSessionRefreshedTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getSessionRefreshedTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("framework-account-accessed");
    }

    @Test
    public void getAuthenticationLoginOrSessionRefreshedVariablesTest() {
        // Arrange
        var username = randomUsername();
        var userRequestMetadata = validUserRequestMetadata();
        var accountAccessType = randomEnum(AccountAccessType.class);

        // Act
        var variables = this.componentUnderTest.getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                accountAccessType
        );

        // Assert
        assertThat(variables).hasSize(7);
        assertThat(variables.get("year")).isEqualTo(now(UTC).getYear());
        assertThat(variables.get("username")).isEqualTo(username.getIdentifier());
        assertThat(variables.get("accessType")).isEqualTo(accountAccessType.getValue());
        assertThat(variables.get("where")).isEqualTo("🇺🇦 Ukraine, Lviv");
        assertThat(variables.get("what")).isEqualTo("Chrome, macOS on Desktop");
        assertThat(variables.get("ipAddress")).isEqualTo("127.0.0.1");
        assertThat(variables.get("webclientURL")).isEqualTo("http://127.0.0.1:3000");
    }
}
