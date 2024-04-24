package io.tech1.framework.emails.utilities.impl;

import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.emails.utilities.EmailUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailUtilityImpl implements EmailUtility {

    // Services
    private final JavaMailSender javaMailSender;

    @Override
    public Tuple2<MimeMessage, MimeMessageHelper> getMimeMessageTuple2() throws MessagingException {
        var message = this.javaMailSender.createMimeMessage();
        var mimeMessageHelper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        return new Tuple2<>(message, mimeMessageHelper);
    }
}
