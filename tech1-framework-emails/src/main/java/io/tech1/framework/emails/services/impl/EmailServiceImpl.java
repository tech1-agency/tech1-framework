package io.tech1.framework.emails.services.impl;

import io.tech1.framework.emails.services.EmailService;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailServiceImpl implements EmailService {

    // Services
    private final JavaMailSender javaMailSender;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void sendPlain(String subject, String message) {
        var emailConfigs = this.applicationFrameworkProperties.getEmailConfigs();
        this.sendPlain(emailConfigs.getTo(), subject, message);
    }

    @Override
    public void sendPlain(String[] to, String subject, String message) {
        var emailConfigs = this.applicationFrameworkProperties.getEmailConfigs();
        if (emailConfigs.isEnabled()) {
            var from = emailConfigs.getFrom();
            var mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            this.javaMailSender.send(mailMessage);
        }
    }

    @Override
    public void sendPlain(List<String> to, String subject, String message) {
        this.sendPlain(to.toArray(new String[0]), subject, message);
    }

    @Override
    public void sendPlain(Set<String> to, String subject, String message) {
        this.sendPlain(to.toArray(new String[0]), subject, message);
    }
}
