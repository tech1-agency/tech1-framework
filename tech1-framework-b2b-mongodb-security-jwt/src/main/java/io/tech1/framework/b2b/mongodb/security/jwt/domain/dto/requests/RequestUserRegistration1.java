package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class RequestUserRegistration1 {
    private final Username username;
    private final Password password;
    private final Password confirmPassword;
    private final String zoneId;
    private final String invitationCode;
}
