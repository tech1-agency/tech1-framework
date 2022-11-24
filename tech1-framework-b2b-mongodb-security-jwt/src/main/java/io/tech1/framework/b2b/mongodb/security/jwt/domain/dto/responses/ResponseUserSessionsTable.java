package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Comparator.comparing;
import static org.springframework.util.CollectionUtils.isEmpty;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ResponseUserSessionsTable {
    private final List<ResponseUserSession2> sessions;
    private final boolean anyPresent;
    private final boolean anyProblem;

    public ResponseUserSessionsTable(List<ResponseUserSession2> sessions) {
        assertNonNullOrThrow(sessions, invalidAttribute("ResponseUserSessionsTable.session"));
        sessions.sort(comparing(ResponseUserSession2::isCurrent).reversed().thenComparing(ResponseUserSession2::getWhere));
        this.sessions = sessions;
        this.anyPresent = !isEmpty(this.sessions);
        this.anyProblem = sessions.stream().anyMatch(session -> !session.getException().isOk());
    }
}
