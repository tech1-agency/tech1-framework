package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.Data;

// Lombok
@Data
public class RequestUserLogin {
    private final String username;
    private final String password;
}
