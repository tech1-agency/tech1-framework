package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserEmailService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.UserEmailUtility;
import io.tech1.framework.domain.properties.configs.SecurityJwtConfigs;
import io.tech1.framework.emails.domain.EmailHTML;
import io.tech1.framework.emails.services.EmailService;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
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

import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.SECURITY_JWT_CONFIGS;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserEmailServiceImplTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        EmailService emailService() {
            return mock(EmailService.class);
        }

        @Bean
        UserEmailUtility userEmailUtility() {
            return mock(UserEmailUtility.class);
        }

        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        public UserEmailService userEmailService() {
            return new UserEmailServiceImpl(
                    this.emailService(),
                    this.userEmailUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Services
    private final EmailService emailService;
    // Utilities
    private final UserEmailUtility userEmailUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final UserEmailService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.emailService,
                this.userEmailUtility,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.emailService,
                this.userEmailUtility,
                this.applicationFrameworkProperties
        );
    }

    @Test
    void noExecutionNullEmailAndEnabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                randomUsername(),
                null,
                randomUserRequestMetadata()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SECURITY_JWT_CONFIGS);

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtility).getAuthenticationLoginTemplateName();
    }

    @Test
    void noExecutionNullEmailAndDisabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                randomUsername(),
                null,
                randomUserRequestMetadata()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.disabledUsersEmailsConfigs());

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtility).getAuthenticationLoginTemplateName();
    }

    @Test
    void noExecutionNotNullEmailAndDisabledEmailTest() {
        // Arrange
        var function = new FunctionAuthenticationLoginEmail(
                randomUsername(),
                randomEmail(),
                randomUserRequestMetadata()
        );
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.disabledUsersEmailsConfigs());

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtility).getAuthenticationLoginTemplateName();
    }

    @Test
    void executeAuthenticationLoginTest() {
        // Arrange
        var username = randomUsername();
        var email = randomEmail();
        var userRequestMetadata = randomUserRequestMetadata();
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
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SECURITY_JWT_CONFIGS);
        when(this.userEmailUtility.getAuthenticationLoginTemplateName()).thenReturn("framework-account-accessed");
        when(this.userEmailUtility.getSubject(eq("Account Accessed"))).thenReturn(subject);
        when(this.userEmailUtility.getAuthenticationLoginOrSessionRefreshedVariables(
                eq(username),
                eq(userRequestMetadata),
                eq(AccountAccessMethod.USERNAME_PASSWORD)
        )).thenReturn(variables);

        // Act
        this.componentUnderTest.executeAuthenticationLogin(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtility).getAuthenticationLoginTemplateName();
        verify(this.userEmailUtility).getSubject(eq("Account Accessed"));
        verify(this.userEmailUtility).getAuthenticationLoginOrSessionRefreshedVariables(
                eq(username),
                eq(userRequestMetadata),
                eq(AccountAccessMethod.USERNAME_PASSWORD)
        );
        var emailHTMLAC = ArgumentCaptor.forClass(EmailHTML.class);
        verify(this.emailService).sendHTML(emailHTMLAC.capture());
        var emailHTML = emailHTMLAC.getValue();
        assertThat(emailHTML.getTo()).hasSize(1);
        assertThat(emailHTML.getTo().stream().iterator().next()).isEqualTo(email.getValue());
        assertThat(emailHTML.getSubject()).isEqualTo(subject);
        assertThat(emailHTML.getTemplateName()).isEqualTo("framework-account-accessed");
        assertThat(emailHTML.getTemplateVariables()).isEqualTo(variables);
    }

    @Test
    void executeSessionRefreshedTest() {
        // Arrange
        var username = randomUsername();
        var email = randomEmail();
        var userRequestMetadata = randomUserRequestMetadata();
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
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SECURITY_JWT_CONFIGS);
        when(this.userEmailUtility.getSessionRefreshedTemplateName()).thenReturn("framework-account-accessed");
        when(this.userEmailUtility.getSubject(eq("Account Accessed"))).thenReturn(subject);
        when(this.userEmailUtility.getAuthenticationLoginOrSessionRefreshedVariables(
                eq(username),
                eq(userRequestMetadata),
                eq(AccountAccessMethod.SECURITY_TOKEN)
        )).thenReturn(variables);

        // Act
        this.componentUnderTest.executeSessionRefreshed(function);

        // Assert
        verify(this.applicationFrameworkProperties).getSecurityJwtConfigs();
        verify(this.userEmailUtility).getSessionRefreshedTemplateName();
        verify(this.userEmailUtility).getSubject(eq("Account Accessed"));
        verify(this.userEmailUtility).getAuthenticationLoginOrSessionRefreshedVariables(
                eq(username),
                eq(userRequestMetadata),
                eq(AccountAccessMethod.SECURITY_TOKEN)
        );
        var emailHTMLAC = ArgumentCaptor.forClass(EmailHTML.class);
        verify(this.emailService).sendHTML(emailHTMLAC.capture());
        var emailHTML = emailHTMLAC.getValue();
        assertThat(emailHTML.getTo()).hasSize(1);
        assertThat(emailHTML.getTo().stream().iterator().next()).isEqualTo(email.getValue());
        assertThat(emailHTML.getSubject()).isEqualTo(subject);
        assertThat(emailHTML.getTemplateName()).isEqualTo("framework-account-accessed");
        assertThat(emailHTML.getTemplateVariables()).isEqualTo(variables);
    }
}
