package io.tech1.framework.emails.services;

import io.tech1.framework.emails.domain.EmailHTML;
import io.tech1.framework.emails.domain.EmailPlainAttachment;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

public interface EmailService {
    @Async
    void sendPlain(String subject, String message);
    @Async
    void sendPlain(String[] to, String subject, String message);
    @Async
    void sendPlain(List<String> to, String subject, String message);
    @Async
    void sendPlain(Set<String> to, String subject, String message);

    @Async
    void sendPlainAttachment(EmailPlainAttachment emailPlainAttachment);

    @Async
    void sendHTML(EmailHTML emailHTML);
}
