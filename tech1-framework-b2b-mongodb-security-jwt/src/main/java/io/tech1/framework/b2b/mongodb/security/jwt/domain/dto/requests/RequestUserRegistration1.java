package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.Data;

// Lombok
@Data
public class RequestUserRegistration1 {
    private final String username;
    private final String password;
    private final String confirmPassword;
    private final String zoneId;
    private final String invitationCode;
}
