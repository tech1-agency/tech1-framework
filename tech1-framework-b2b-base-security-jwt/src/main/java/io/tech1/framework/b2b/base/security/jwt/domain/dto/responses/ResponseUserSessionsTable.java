package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static org.springframework.util.CollectionUtils.isEmpty;

public record ResponseUserSessionsTable(
        List<ResponseUserSession2> sessions,
        boolean anyPresent,
        boolean anyProblem
) {
    public static ResponseUserSessionsTable of(List<ResponseUserSession2> sessions) {
        sessions.sort(comparing(ResponseUserSession2::current).reversed().thenComparing(ResponseUserSession2::where));
        return new ResponseUserSessionsTable(
                sessions,
                !isEmpty(sessions),
                sessions.stream().anyMatch(session -> !session.exception().isOk())
        );
    }

    public static ResponseUserSessionsTable random() {
        var sessions = new ArrayList<ResponseUserSession2>();
        sessions.add(ResponseUserSession2.testsHardcodedCurrent());
        IntStream.range(0, 100).forEach(element -> sessions.add(ResponseUserSession2.random()));
        return of(sessions);
    }
}
