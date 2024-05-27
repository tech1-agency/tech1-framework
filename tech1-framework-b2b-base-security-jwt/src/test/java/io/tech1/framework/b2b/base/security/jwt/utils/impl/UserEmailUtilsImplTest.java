package io.tech1.framework.b2b.base.security.jwt.utils.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.b2b.base.security.jwt.utils.UserEmailUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.utilities.time.LocalDateTimeUtility;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import lombok.RequiredArgsConstructor;
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

import static io.tech1.framework.domain.constants.DatetimeConstants.DTF11;
import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.FIVE_TIMES;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEnum;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.getTimestamp;
import static io.tech1.framework.domain.utilities.time.LocalDateUtility.now;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserEmailUtilsImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ResourceLoader resourceLoader;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        UserEmailUtils userEmailUtility() {
            return new UserEmailUtilsImpl(
                    this.resourceLoader,
                    this.applicationFrameworkProperties
            );
        }
    }

    private final UserEmailUtils componentUnderTest;

    @RepeatedTest(FIVE_TIMES)
    void getSubjectTest() {
        // Act
        var subject = this.componentUnderTest.getSubject("Authentication Login");

        // Assert
        assertThat(subject)
                .startsWith("[Tech1] Authentication Login on \"tech1-server\" — ")
                .endsWith(" (UTC)");
        subject = subject.replace("[Tech1] Authentication Login on \"tech1-server\" — ", "");
        subject = subject.replace(" (UTC)", "");
        var timestamp = getTimestamp(LocalDateTimeUtility.parse(subject, DTF11), ZoneOffset.UTC);
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
        var username = Username.random();
        var userRequestMetadata = UserRequestMetadata.valid();
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
                .containsEntry("username", username.value())
                .containsEntry("accessMethod", accountAccessMethod.getValue())
                .containsEntry("where", "🇺🇦 Ukraine, Lviv")
                .containsEntry("what", "Chrome, macOS on Desktop")
                .containsEntry("ipAddress", "127.0.0.1")
                .containsEntry("webclientURL", "http://127.0.0.1:3000");
    }
}
