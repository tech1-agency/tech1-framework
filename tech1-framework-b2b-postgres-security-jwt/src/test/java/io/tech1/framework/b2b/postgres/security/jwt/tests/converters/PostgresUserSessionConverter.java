package io.tech1.framework.b2b.postgres.security.jwt.tests.converters;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PostgresUserSessionConverter {

    public static Set<Boolean> toMetadataRenewCron(List<PostgresDbUserSession> sessions) {
        return sessions.stream().map(PostgresDbUserSession::isMetadataRenewCron).collect(Collectors.toSet());
    }
}
