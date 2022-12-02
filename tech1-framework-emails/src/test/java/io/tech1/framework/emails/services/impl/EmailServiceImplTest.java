package io.tech1.framework.emails.services.impl;

import io.tech1.framework.domain.properties.configs.EmailConfigs;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEmail;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailServiceImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        JavaMailSender javaMailSender() {
            return mock(JavaMailSender.class);
        }

        @Bean
        EmailService emailService() {
            return new EmailServiceImpl(
                    this.javaMailSender(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Services
    private final JavaMailSender javaMailSender;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final EmailService componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.javaMailSender,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.javaMailSender,
                this.applicationFrameworkProperties
        );
    }

    @Test
    public void mainSendPlainDisabledTest() {
        // Arrange
        var to = randomEmail();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(false);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(new String[] { to }, subject, message);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
    }

    @Test
    public void mainSendPlainEnabledTest() {
        // Arrange
        var to = randomEmail();
        var from = randomEmail();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(new String[] { to }, subject, message);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
        var mailMessageAC = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(this.javaMailSender).send(mailMessageAC.capture());
        var simpleMailMessage = mailMessageAC.getValue();
        assertThat(simpleMailMessage.getTo()).isEqualTo(new String[] { to });
        assertThat(simpleMailMessage.getSubject()).isEqualTo(subject);
        assertThat(simpleMailMessage.getText()).isEqualTo(message);
        assertThat(simpleMailMessage.getFrom()).isEqualTo(from);
    }

    @Test
    public void systemSendPlainEnabledTest() {
        // Arrange
        var to1 = randomEmail();
        var to2 = randomEmail();
        var from = randomEmail();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        emailConfigs.setTo(new String[] { to1, to2 } );
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(subject, message);

        // Assert
        verify(this.applicationFrameworkProperties, times(2)).getEmailConfigs();
        var mailMessageAC = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(this.javaMailSender).send(mailMessageAC.capture());
        var simpleMailMessage = mailMessageAC.getValue();
        assertThat(simpleMailMessage.getTo()).isEqualTo(new String[] { to1, to2 });
        assertThat(simpleMailMessage.getSubject()).isEqualTo(subject);
        assertThat(simpleMailMessage.getText()).isEqualTo(message);
        assertThat(simpleMailMessage.getFrom()).isEqualTo(from);
    }

    @Test
    public void listSendPlainEnabledTest() {
        // Arrange
        var to = randomEmail();
        var from = randomEmail();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(List.of(to), subject, message);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
        var mailMessageAC = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(this.javaMailSender).send(mailMessageAC.capture());
        var simpleMailMessage = mailMessageAC.getValue();
        assertThat(simpleMailMessage.getTo()).isEqualTo(new String[] { to });
        assertThat(simpleMailMessage.getSubject()).isEqualTo(subject);
        assertThat(simpleMailMessage.getText()).isEqualTo(message);
        assertThat(simpleMailMessage.getFrom()).isEqualTo(from);
    }

    @Test
    public void setSendPlainEnabledTest() {
        // Arrange
        var to = randomEmail();
        var from = randomEmail();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(Set.of(to), subject, message);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
        var mailMessageAC = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(this.javaMailSender).send(mailMessageAC.capture());
        var simpleMailMessage = mailMessageAC.getValue();
        assertThat(simpleMailMessage.getTo()).isEqualTo(new String[] { to });
        assertThat(simpleMailMessage.getSubject()).isEqualTo(subject);
        assertThat(simpleMailMessage.getText()).isEqualTo(message);
        assertThat(simpleMailMessage.getFrom()).isEqualTo(from);
    }
}
