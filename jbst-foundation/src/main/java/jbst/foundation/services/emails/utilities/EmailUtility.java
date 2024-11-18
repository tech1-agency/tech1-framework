package jbst.foundation.services.emails.utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jbst.foundation.domain.tuples.Tuple2;
import org.springframework.mail.javamail.MimeMessageHelper;

public interface EmailUtility {
    Tuple2<MimeMessage, MimeMessageHelper> getMimeMessageTuple2() throws MessagingException;
}
