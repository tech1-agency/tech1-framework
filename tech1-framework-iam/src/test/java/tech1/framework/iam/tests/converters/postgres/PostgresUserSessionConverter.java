package tech1.framework.iam.tests.converters.postgres;

import tech1.framework.iam.domain.postgres.db.PostgresDbUserSession;
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
