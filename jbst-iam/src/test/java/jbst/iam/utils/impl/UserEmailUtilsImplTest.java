package jbst.iam.utils.impl;

import jbst.foundation.configurations.TestConfigurationPropertiesJbstHardcoded;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.utilities.random.RandomUtility;
import jbst.foundation.utilities.time.LocalDateTimeUtility;
import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.iam.utils.UserEmailUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.ZoneOffset;

import static java.time.ZoneOffset.UTC;
import static jbst.foundation.domain.constants.JbstConstants.DateTimeFormatters.DTF11;
import static jbst.foundation.domain.tests.constants.TestsJunitConstants.FIVE_TIMES;
import static jbst.foundation.utilities.random.RandomUtility.randomEnum;
import static jbst.foundation.utilities.time.LocalDateTimeUtility.getTimestamp;
import static jbst.foundation.utilities.time.LocalDateUtility.now;
import static jbst.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserEmailUtilsImplTest {

    @Configuration
    @Import({
            TestConfigurationPropertiesJbstHardcoded.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ResourceLoader resourceLoader;
        private final JbstProperties jbstProperties;

        @Bean
        public ServerProperties serverProperties() {
            var serverProperties = new ServerProperties();
            serverProperties.getServlet().setContextPath("/api");
            return serverProperties;
        }

        @Bean
        UserEmailUtils userEmailUtility() {
            return new UserEmailUtilsImpl(
                    this.resourceLoader,
                    this.jbstProperties,
                    this.serverProperties()
            );
        }
    }

    private final UserEmailUtils componentUnderTest;

    @RepeatedTest(FIVE_TIMES)
    void getSubjectTest() {
        // Act
        var subject = this.componentUnderTest.getSubject("Account Accessed");

        // Assert
        assertThat(subject)
                .startsWith("[jbst.com] Account Accessed at ")
                .endsWith(" (UTC)");
        subject = subject.replace("[jbst.com] Account Accessed at ", "");
        subject = subject.replace(" (UTC)", "");
        var timestamp = getTimestamp(LocalDateTimeUtility.parse(subject, DTF11), ZoneOffset.UTC);
        assertThat(getCurrentTimestamp() - timestamp).isBetween(0L, 2000L);
    }

    @Test
    void getConfirmEmailTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getConfirmEmailTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("jbst-confirm-email");
    }

    @Test
    void getResetPasswordTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getResetPasswordTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("jbst-reset-password");
    }

    @Test
    void getAuthenticationLoginTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getAuthenticationLoginTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("jbst-account-accessed");
    }

    @Test
    void getSessionRefreshedTemplateNameTest() {
        // Act
        var templateName = this.componentUnderTest.getSessionRefreshedTemplateName();

        // Assert
        assertThat(templateName).isEqualTo("jbst-account-accessed");
    }

    @Test
    void getConfirmEmailVariablesTest() {
        // Arrange
        var username = Username.hardcoded();
        var token = RandomUtility.randomStringLetterOrNumbersOnly(36);
        var confirmationLink = "http://127.0.0.1:3002/api/jbst/security/tokens/email/confirm?token=" + token;

        // Act
        var actual = this.componentUnderTest.getConfirmEmailVariables(
                username,
                token
        );

        // Assert
        assertThat(actual)
                .hasSize(3)
                .containsEntry("username", username.value())
                .containsEntry("confirmationLink", confirmationLink)
                .containsEntry("year", now(UTC).getYear());

    }

    @Test
    void getResetPasswordVariablesTest() {
        // Arrange
        var username = Username.hardcoded();
        var token = RandomUtility.randomStringLetterOrNumbersOnly(36);
        var confirmationLink = "http://127.0.0.1:3000/password-reset?token=" + token;

        // Act
        var actual = this.componentUnderTest.getResetPasswordVariables(
                username,
                token
        );

        // Assert
        assertThat(actual)
                .hasSize(3)
                .containsEntry("username", username.value())
                .containsEntry("resetPasswordLink", confirmationLink)
                .containsEntry("year", now(UTC).getYear());

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
