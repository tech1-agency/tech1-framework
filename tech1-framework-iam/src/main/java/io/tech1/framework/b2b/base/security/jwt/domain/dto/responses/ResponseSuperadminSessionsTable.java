package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import java.util.List;

public record ResponseSuperadminSessionsTable(
        List<ResponseUserSession2> activeSessions,
        List<ResponseUserSession2> inactiveSessions
) {
}
