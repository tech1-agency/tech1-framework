package io.tech1.framework.foundation.configurations;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.services.emails.services.EmailService;
import io.tech1.framework.foundation.services.emails.services.impl.EmailServiceImpl;
import io.tech1.framework.foundation.services.emails.utilities.EmailUtility;
import io.tech1.framework.foundation.services.emails.utilities.impl.EmailUtilityImpl;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEmails {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getEmailConfigs().assertProperties(new PropertyId("emailConfigs"));
    }

    @Bean
    public JavaMailSender javaMailSender() {
        var emailConfigs = this.applicationFrameworkProperties.getEmailConfigs();

        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfigs.getHost());
        mailSender.setPort(emailConfigs.getPort());

        mailSender.setUsername(emailConfigs.getUsername().value());
        mailSender.setPassword(emailConfigs.getPassword().value());

        var props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        var emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/email-templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }

    @Bean
    public EmailUtility emailUtility() {
        return new EmailUtilityImpl(
                this.javaMailSender()
        );
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(
                this.javaMailSender(),
                this.springTemplateEngine(),
                this.emailUtility(),
                this.applicationFrameworkProperties
        );
    }
}
