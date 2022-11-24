package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import lombok.Data;

import java.util.Set;

// Lombok
@Data
public class RequestNewInvitationCodeParams {
    private final Set<String> authorities;
}
