package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class RequestUserUpdate1 {
    private final String zoneId;
    private final String email;
    private final String name;
}
