package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import java.util.Set;

public record RequestNewInvitationCodeParams(
        Set<String> authorities
) {
}
