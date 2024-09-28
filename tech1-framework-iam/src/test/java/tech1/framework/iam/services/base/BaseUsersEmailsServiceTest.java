package tech1.framework.iam.services.base;

import tech1.framework.iam.domain.enums.AccountAccessMethod;
import tech1.framework.iam.domain.functions.FunctionAuthenticationLoginEmail;
import tech1.framework.iam.domain.functions.FunctionSessionRefreshedEmail;
import tech1.framework.iam.services.UsersEmailsService;
import tech1.framework.iam.utils.UserEmailUtils;
import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.properties.configs.SecurityJwtConfigs;
import tech1.framework.foundation.services.emails.domain.EmailHTML;
import tech1.framework.foundation.services.emails.services.EmailService;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Map;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseUsersEmailsServiceTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        EmailService emailService() {
            return mock(EmailService.class);
        }

        @Bean
        UserEmailUtils userEmailUtility() {
            return mock(UserEmailUtils.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        public UsersEmailsService userEmailService() {
            return new BaseUsersEmailsService(
                    this.emailService(),
                    this.userEmailUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Services
    private final EmailService emailService;
    // Utilities
    private final UserEmailUtils userEmailUtils;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final UsersEmailsService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.emailService,
                this.userEmailUtils,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.emailService,
                this.userEmailUtils,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void noExecutionNullEmailAndEnabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                Username.random(),
                null,
                UserRequestMetadata.random()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.testsHardcoded());

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtils).getAuthenticationLoginTemplateName();
    }

    @Test
    void noExecutionNullEmailAndDisabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                Username.random(),
                null,
                UserRequestMetadata.random()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.disabledUsersEmailsConfigs());

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtils).getAuthenticationLoginTemplateName();
    }

    @Test
    void noExecutionNotNullEmailAndDisabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                Username.random(),
                Email.random(),
                UserRequestMetadata.random()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.disabledUsersEmailsConfigs());

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtils).getAuthenticationLoginTemplateName();
    }

    @Test
    void executeAuthenticationLoginTest() {
        // Arrange
        var username = Username.random();
        var email = Email.random();
        var userRequestMetadata = UserRequestMetadata.random();
        var function = new FunctionAuthenticationLoginEmail(
                username,
                email,
                userRequestMetadata
        );
        var subject = randomString();
        var variables = Map.of(
                randomString(), new Object(),
                randomString(), new Object(),
                randomString(), new Object()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.testsHardcoded());
        when(this.userEmailUtils.getAuthenticationLoginTemplateName()).thenReturn("framework-account-accessed");
        when(this.userEmailUtils.getSubject("Account Accessed")).thenReturn(subject);
        when(this.userEmailUtils.getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                AccountAccessMethod.USERNAME_PASSWORD
        )).thenReturn(variables);

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtils).getAuthenticationLoginTemplateName();
        verify(this.userEmailUtils).getSubject("Account Accessed");
        verify(this.userEmailUtils).getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                AccountAccessMethod.USERNAME_PASSWORD
        );
        var emailHTMLAC = ArgumentCaptor.forClass(EmailHTML.class);
        verify(this.emailService).sendHTML(emailHTMLAC.capture());
        var emailHTML = emailHTMLAC.getValue();
        assertThat(emailHTML.to()).hasSize(1);
        assertThat(emailHTML.to().stream().iterator().next()).isEqualTo(email.value());
        assertThat(emailHTML.subject()).isEqualTo(subject);
        assertThat(emailHTML.templateName()).isEqualTo("framework-account-accessed");
        assertThat(emailHTML.templateVariables()).isEqualTo(variables);
    }

    @Test
    void executeSessionRefreshedTest() {
        // Arrange
        var username = Username.random();
        var email = Email.random();
        var userRequestMetadata = UserRequestMetadata.random();
        var function = new FunctionSessionRefreshedEmail(
                username,
                email,
                userRequestMetadata
        );
        var subject = randomString();
        var variables = Map.of(
                randomString(), new Object(),
                randomString(), new Object(),
                randomString(), new Object()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.testsHardcoded());
        when(this.userEmailUtils.getSessionRefreshedTemplateName()).thenReturn("framework-account-accessed");
        when(this.userEmailUtils.getSubject("Account Accessed")).thenReturn(subject);
        when(this.userEmailUtils.getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                AccountAccessMethod.SECURITY_TOKEN
        )).thenReturn(variables);

        // Act
        this.componentUnderTest.executeSessionRefreshed(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtils).getSessionRefreshedTemplateName();
        verify(this.userEmailUtils).getSubject("Account Accessed");
        verify(this.userEmailUtils).getAuthenticationLoginOrSessionRefreshedVariables(
                username,
                userRequestMetadata,
                AccountAccessMethod.SECURITY_TOKEN
        );
        var emailHTMLAC = ArgumentCaptor.forClass(EmailHTML.class);
        verify(this.emailService).sendHTML(emailHTMLAC.capture());
        var emailHTML = emailHTMLAC.getValue();
        assertThat(emailHTML.to()).hasSize(1);
        assertThat(emailHTML.to().stream().iterator().next()).isEqualTo(email.value());
        assertThat(emailHTML.subject()).isEqualTo(subject);
        assertThat(emailHTML.templateName()).isEqualTo("framework-account-accessed");
        assertThat(emailHTML.templateVariables()).isEqualTo(variables);
    }
}
