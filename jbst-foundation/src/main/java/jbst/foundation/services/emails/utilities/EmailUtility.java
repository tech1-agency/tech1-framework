package jbst.foundation.services.emails.utilities;

import jbst.foundation.domain.tuples.Tuple2;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailUtility {
    Tuple2<MimeMessage, MimeMessageHelper> getMimeMessageTuple2() throws MessagingException;
}
