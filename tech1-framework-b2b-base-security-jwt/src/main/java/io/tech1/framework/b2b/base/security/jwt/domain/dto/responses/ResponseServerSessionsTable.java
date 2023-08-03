package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.base.Username;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.SESSIONS22;

public record ResponseServerSessionsTable(
        List<ResponseUserSession2> activeSessions,
        List<ResponseUserSession2> inactiveSessions
) {
    public static ResponseServerSessionsTable of(
            List<ResponseUserSession2> activeSessions,
            List<ResponseUserSession2> inactiveSessions
    ) {
        activeSessions.sort(SESSIONS22);
        inactiveSessions.sort(SESSIONS22);
        return new ResponseServerSessionsTable(
                activeSessions,
                inactiveSessions
        );
    }

    @JsonIgnore
    public Set<Username> getActiveUsernames() {
        return this.activeSessions.stream().map(ResponseUserSession2::who).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<Username> getInactiveUsernames() {
        return this.inactiveSessions.stream().map(ResponseUserSession2::who).collect(Collectors.toSet());
    }
}
