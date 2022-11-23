package io.tech1.framework.emails.configurations;

import io.tech1.framework.emails.services.EmailService;
import io.tech1.framework.emails.services.impl.EmailServiceImpl;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEmails {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        assertProperties(this.applicationFrameworkProperties.getEmailConfigs(), "emailConfigs");
    }

    @Bean
    public JavaMailSender javaMailSender() {
        var emailConfigs = this.applicationFrameworkProperties.getEmailConfigs();

        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfigs.getHost());
        mailSender.setPort(emailConfigs.getPort());

        mailSender.setUsername(emailConfigs.getUsername());
        mailSender.setPassword(emailConfigs.getPassword());

        var props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(
                this.javaMailSender(),
                this.applicationFrameworkProperties
        );
    }
}
