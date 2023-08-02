package io.tech1.framework.b2b.postgres.security.jwt.tests.converters;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.tests.utilities.BaseSecurityJwtJunitUtility.toUsernamesAsStrings0;

@UtilityClass
public class UserConverter {

    public static List<String> toUsernamesAsStrings1(List<PostgresDbUser> users) {
        return toUsernamesAsStrings0(users.stream().map(PostgresDbUser::getUsername).collect(Collectors.toList()));
    }
}
