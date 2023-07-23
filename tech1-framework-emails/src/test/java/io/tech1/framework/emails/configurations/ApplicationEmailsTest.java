package io.tech1.framework.emails.configurations;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationEmailsTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class,
            ApplicationEmails.class
    })
    static class ContextConfiguration {

    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final ApplicationEmails componentUnderTest;

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods).contains("javaMailSender");
        assertThat(methods).contains("springTemplateEngine");
        assertThat(methods).contains("htmlTemplateResolver");
        assertThat(methods).contains("emailUtility");
        assertThat(methods).contains("emailService");
        assertThat(methods).hasSize(19);
    }

    @Test
    void javaMailSenderTest() {
        // Act
        var javaMailSender = (JavaMailSenderImpl) this.componentUnderTest.javaMailSender();

        // Assert
        var emailConfigs = this.applicationFrameworkProperties.getEmailConfigs();
        assertThat(javaMailSender.getHost()).isEqualTo(emailConfigs.getHost());
        assertThat(javaMailSender.getPort()).isEqualTo(emailConfigs.getPort());
        assertThat(javaMailSender.getUsername()).isEqualTo(emailConfigs.getUsername());
        assertThat(javaMailSender.getPassword()).isEqualTo(emailConfigs.getPassword());
        assertThat(javaMailSender.getJavaMailProperties()).hasSize(4);
        assertThat(javaMailSender.getJavaMailProperties().get("mail.transport.protocol")).isEqualTo("smtp");
        assertThat(javaMailSender.getJavaMailProperties().get("mail.smtp.auth")).isEqualTo("true");
        assertThat(javaMailSender.getJavaMailProperties().get("mail.smtp.starttls.enable")).isEqualTo("true");
        assertThat(javaMailSender.getJavaMailProperties().get("mail.debug")).isEqualTo("false");
    }
}
