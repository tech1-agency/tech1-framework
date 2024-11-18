package jbst.foundation.services.emails.services;

import jbst.foundation.services.emails.domain.EmailHTML;
import jbst.foundation.services.emails.domain.EmailPlainAttachment;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

@Async
public interface EmailService {
    void sendPlain(String[] to, String subject, String message);
    void sendPlain(List<String> to, String subject, String message);
    void sendPlain(Set<String> to, String subject, String message);

    void sendPlainAttachment(EmailPlainAttachment emailPlainAttachment);

    void sendHTML(EmailHTML emailHTML);
}
