package io.tech1.framework.emails.domain;

import lombok.Data;

import java.util.Set;

@Data
public class EmailPlainAttachment {
    private final Set<String> to;
    private final String subject;
    private final String message;
    private final String attachmentFileName;
    private final String attachmentMessage;
}
