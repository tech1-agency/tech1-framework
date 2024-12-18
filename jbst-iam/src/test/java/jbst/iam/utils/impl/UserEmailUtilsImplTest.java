package jbst.iam.utils.impl;

import jbst.foundation.configurations.TestConfigurationPropertiesJbstHardcoded;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.utilities.time.LocalDateTimeUtility;
import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.iam.domain.functions.FunctionAccountAccessed;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.domain.functions.FunctionPasswordReset;
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
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static jbst.foundation.domain.constants.JbstConstants.DateTimeFormatters.DTF11;
import static jbst.foundation.domain.tests.constants.TestsJunitConstants.FIVE_TIMES;
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
    void getAccountAccessedHTML() {
        // Act
        var emailHTML = this.componentUnderTest.getAccountAccessedHTML(FunctionAccountAccessed.hardcoded());

        // Assert
        assertThat(emailHTML.to()).isEqualTo(Set.of(Email.hardcoded().value()));
        assertThat(emailHTML.subject()).startsWith("[jbst.com] Account Accessed at ");
        assertThat(emailHTML.templateName()).isEqualTo("jbst-account-accessed");
        assertThat(emailHTML.templateVariables())
                .hasSize(7)
                .containsEntry("year", now(UTC).getYear())
                .containsEntry("username", Username.hardcoded().value())
                .containsEntry("accessMethod", AccountAccessMethod.USERNAME_PASSWORD.getValue())
                .containsEntry("where", "🇺🇦 Ukraine, Lviv")
                .containsEntry("what", "Chrome, macOS on Desktop")
                .containsEntry("ipAddress", "127.0.0.1")
                .containsEntry("webclientURL", "http://127.0.0.1:3000");
    }

    @Test
    void getEmailConfirmationHTML() {
        // Arrange
        var function = FunctionEmailConfirmation.hardcoded();

        // Act
        var emailHTML = this.componentUnderTest.getEmailConfirmationHTML(FunctionEmailConfirmation.hardcoded());

        // Assert
        assertThat(emailHTML.to()).isEqualTo(Set.of(function.email().value()));
        assertThat(emailHTML.subject()).startsWith("[jbst.com] Email Confirmation at ");
        assertThat(emailHTML.templateName()).isEqualTo("jbst-email-confirmation");
        assertThat(emailHTML.templateVariables())
                .hasSize(3)
                .containsEntry("username", function.username().value())
                .containsEntry("emailConfirmationLink", "http://127.0.0.1:3002/api/jbst/security/tokens/email/confirm?token=" + function.token())
                .containsEntry("year", now(UTC).getYear());
    }

    @Test
    void getPasswordResetHTML() {
        // Arrange
        var function = FunctionPasswordReset.hardcoded();

        // Act
        var emailHTML = this.componentUnderTest.getPasswordResetHTML(function);

        // Assert
        assertThat(emailHTML.to()).isEqualTo(Set.of(function.email().value()));
        assertThat(emailHTML.subject()).startsWith("[jbst.com] Password Reset at ");
        assertThat(emailHTML.templateName()).isEqualTo("jbst-password-reset");
        assertThat(emailHTML.templateVariables())
                .hasSize(3)
                .containsEntry("username", function.username().value())
                .containsEntry("resetPasswordLink", "http://127.0.0.1:3000/password-reset?token=" + function.token())
                .containsEntry("year", now(UTC).getYear());
    }
}
