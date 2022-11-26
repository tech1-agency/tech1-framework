package io.tech1.framework.emails.services;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

public interface EmailService {
    @Async
    void send(String subject, String message);
    @Async
    void send(String[] to, String subject, String message);
    @Async
    void send(List<String> to, String subject, String message);
    @Async
    void send(Set<String> to, String subject, String message);
}
