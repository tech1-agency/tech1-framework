package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.mongodb.security.jwt.comparators.SecurityJwtComparators;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ResponseServerSessionsTable {
    private final List<ResponseUserSession3> activeSessions;
    private final List<ResponseUserSession3> inactiveSessions;

    public ResponseServerSessionsTable(
            List<ResponseUserSession3> activeSessions,
            List<ResponseUserSession3> inactiveSessions
    ) {
        this.activeSessions = activeSessions;
        this.activeSessions.sort(SecurityJwtComparators.SESSIONS_3);
        this.inactiveSessions = inactiveSessions;
        this.inactiveSessions.sort(SecurityJwtComparators.SESSIONS_3);
    }

    @JsonIgnore
    public Set<Username> getActiveUsernames() {
        return this.activeSessions.stream().map(ResponseUserSession3::getWho).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<Username> getInactiveUsernames() {
        return this.inactiveSessions.stream().map(ResponseUserSession3::getWho).collect(Collectors.toSet());
    }
}
