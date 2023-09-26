package io.tech1.framework.b2b.mongodb.security.jwt.tests.converters;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class MongoUserSessionConverter {

    public static Set<Boolean> toMetadataRenewCron(List<MongoDbUserSession> sessions) {
        return sessions.stream().map(MongoDbUserSession::isMetadataRenewCron).collect(Collectors.toSet());
    }
}
