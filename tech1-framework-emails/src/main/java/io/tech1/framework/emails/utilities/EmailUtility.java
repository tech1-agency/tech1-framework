package io.tech1.framework.emails.utilities;

import io.tech1.framework.domain.tuples.Tuple2;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailUtility {
    Tuple2<MimeMessage, MimeMessageHelper> getMimeMessageTuple2() throws MessagingException;
}
