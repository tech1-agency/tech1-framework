package jbst.iam.domain.dto.responses;

import java.util.List;

public record ResponseSuperadminSessionsTable(
        List<ResponseUserSession2> activeSessions,
        List<ResponseUserSession2> inactiveSessions
) {
}
