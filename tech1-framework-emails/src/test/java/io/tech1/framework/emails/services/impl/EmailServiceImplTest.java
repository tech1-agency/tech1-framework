package io.tech1.framework.emails.services.impl;

import io.tech1.framework.domain.properties.configs.EmailConfigs;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.emails.domain.EmailHTML;
import io.tech1.framework.emails.services.EmailService;
import io.tech1.framework.emails.utilities.EmailUtility;
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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEmailAsValue;
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
        SpringTemplateEngine springTemplateEngine() {
            var templateEngine = new SpringTemplateEngine();
            templateEngine.addTemplateResolver(htmlTemplateResolver());
            return templateEngine;
        }

        @Bean
        SpringResourceTemplateResolver htmlTemplateResolver() {
            var emailTemplateResolver = new SpringResourceTemplateResolver();
            emailTemplateResolver.setPrefix("classpath:/test-email-templates/");
            emailTemplateResolver.setSuffix(".html");
            emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
            emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
            return emailTemplateResolver;
        }

        @Bean
        EmailUtility emailUtility() {
            return mock(EmailUtility.class);
        }

        @Bean
        EmailService emailService() {
            return new EmailServiceImpl(
                    this.javaMailSender(),
                    this.springTemplateEngine(),
                    this.emailUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    // Services
    private final JavaMailSender javaMailSender;
    // Utilities
    private final EmailUtility emailUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final EmailService componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.javaMailSender,
                this.emailUtility,
                this.applicationFrameworkProperties
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.javaMailSender,
                this.emailUtility,
                this.applicationFrameworkProperties
        );
    }

    @Test
    public void mainSendPlainDisabledTest() {
        // Arrange
        var to = randomEmailAsValue();
        var subject = randomString();
        var message = randomString();
        var emailConfigs = EmailConfigs.disabled();
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendPlain(new String[] { to }, subject, message);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
    }

    @Test
    public void mainSendPlainEnabledTest() {
        // Arrange
        var to = randomEmailAsValue();
        var from = randomEmailAsValue();
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
        var to1 = randomEmailAsValue();
        var to2 = randomEmailAsValue();
        var from = randomEmailAsValue();
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
        var to = randomEmailAsValue();
        var from = randomEmailAsValue();
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
        var to = randomEmailAsValue();
        var from = randomEmailAsValue();
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

    @Test
    public void sendHTMLDisabledTest() {
        // Arrange
        var emailHTML = entity(EmailHTML.class);
        var emailConfigs = EmailConfigs.disabled();
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);

        // Act
        this.componentUnderTest.sendHTML(emailHTML);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
    }

    @Test
    public void sendHTMLEnabledExceptionTest() throws MessagingException {
        // Arrange
        var from = randomEmailAsValue();
        var emailHTML = entity(EmailHTML.class);
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);
        when(this.emailUtility.getMimeMessageTuple2()).thenThrow(new MessagingException());

        // Act
        this.componentUnderTest.sendHTML(emailHTML);

        // Assert
        verify(this.applicationFrameworkProperties).getEmailConfigs();
        verify(this.emailUtility).getMimeMessageTuple2();
    }

    @Test
    public void sendHTMLEnabledTest() throws MessagingException {
        // Arrange
        var from = randomEmailAsValue();
        Map<String, Object> templateVariables = Map.of(
                "param1", "key2",
                "param2", 2L
        );
        var emailHTML = EmailHTML.of(
                Set.of(
                        "tests@tech1.io"
                ),
                "subject1",
                "template1",
                templateVariables
        );
        var emailConfigs = new EmailConfigs();
        emailConfigs.setEnabled(true);
        emailConfigs.setFrom(from);
        when(this.applicationFrameworkProperties.getEmailConfigs()).thenReturn(emailConfigs);
        var message = mock(MimeMessage.class);
        var mimeMessageHelper = mock(MimeMessageHelper.class);
        when(this.emailUtility.getMimeMessageTuple2()).thenReturn(Tuple2.of(message, mimeMessageHelper));

        // Act
        this.componentUnderTest.sendHTML(emailHTML);

        // Assert
        verify(mimeMessageHelper).setFrom(eq(from));
        verify(mimeMessageHelper).setTo(eq(new String[] { "tests@tech1.io" }));
        verify(mimeMessageHelper).setSubject(eq("subject1"));
        var html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>\n" +
                "  Param1: <span >key2</span>\n" +
                "</p>\n" +
                "<p>\n" +
                "  Param2: <span >2</span>\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>\n";
        verify(mimeMessageHelper).setText(eq(html), eq(true));
        verify(this.applicationFrameworkProperties).getEmailConfigs();
        verify(this.emailUtility).getMimeMessageTuple2();
        verify(this.javaMailSender).send(any(MimeMessage.class));
        verifyNoMoreInteractions(
                message,
                mimeMessageHelper
        );
    }
}
