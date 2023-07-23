package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.base.Username;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.mongodb.security.jwt.comparators.SecurityJwtComparators.SESSIONS_3;

public record ResponseServerSessionsTable(
        List<ResponseUserSession3> activeSessions,
        List<ResponseUserSession3> inactiveSessions
) {
    public static ResponseServerSessionsTable of(
            List<ResponseUserSession3> activeSessions,
            List<ResponseUserSession3> inactiveSessions
    ) {
        activeSessions.sort(SESSIONS_3);
        inactiveSessions.sort(SESSIONS_3);
        return new ResponseServerSessionsTable(
                activeSessions,
                inactiveSessions
        );
    }

    @JsonIgnore
    public Set<Username> getActiveUsernames() {
        return this.activeSessions.stream().map(ResponseUserSession3::who).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<Username> getInactiveUsernames() {
        return this.inactiveSessions.stream().map(ResponseUserSession3::who).collect(Collectors.toSet());
    }
}
