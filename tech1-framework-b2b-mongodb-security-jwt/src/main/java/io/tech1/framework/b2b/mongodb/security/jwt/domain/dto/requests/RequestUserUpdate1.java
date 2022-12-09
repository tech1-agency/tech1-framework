package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Email;
import lombok.Data;

// Lombok
@Data
public class RequestUserUpdate1 {
    private final String zoneId;
    private final Email email;
    private final String name;
}
