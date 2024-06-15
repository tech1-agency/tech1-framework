package io.tech1.framework.b2b.base.security.jwt.comparators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@UtilityClass
public class SecurityJwtComparators {
    public static final Comparator<ResponseUserSession2> USERS_SESSIONS = comparing(ResponseUserSession2::current).reversed()
            .thenComparing(ResponseUserSession2::where);

    public static final Comparator<ResponseUserSession2> ACTIVE_SESSIONS_AS_SUPERADMIN = comparing(ResponseUserSession2::current).reversed()
            .thenComparing((ResponseUserSession2 session) -> session.who().value())
            .thenComparing(ResponseUserSession2::where);

    public static final Comparator<ResponseUserSession2> INACTIVE_SESSIONS_AS_SUPERADMIN = comparing((ResponseUserSession2 session) -> session.who().value())
            .thenComparing(ResponseUserSession2::where);
}
