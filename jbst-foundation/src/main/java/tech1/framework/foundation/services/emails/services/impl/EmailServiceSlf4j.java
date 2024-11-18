package tech1.framework.foundation.services.emails.services.impl;

import tech1.framework.foundation.services.emails.domain.EmailHTML;
import tech1.framework.foundation.services.emails.domain.EmailPlainAttachment;
import tech1.framework.foundation.services.emails.services.EmailService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class EmailServiceSlf4j implements EmailService {

    @Override
    public void sendPlain(String[] to, String subject, String message) {
        this.logPlain(to, subject, message);
    }

    @Override
    public void sendPlain(List<String> to, String subject, String message) {
        this.logPlain(to.toArray(new String[]{}), subject, message);
    }

    @Override
    public void sendPlain(Set<String> to, String subject, String message) {
        this.logPlain(to.toArray(new String[]{}), subject, message);
    }

    @Override
    public void sendPlainAttachment(EmailPlainAttachment emailPlainAttachment) {
        LOGGER.info("Send email attachment: {}", emailPlainAttachment);
    }

    @Override
    public void sendHTML(EmailHTML emailHTML) {
        LOGGER.info("Send email HTML: {}", emailHTML);
    }

    // ================================================================================================================
    // PRIVATE METHODS
    // ================================================================================================================
    private void logPlain(String[] to, String subject, String message) {
        LOGGER.info("Send email. To: {}. Subject: {}. Message: {}", to, subject, message);
    }

}
