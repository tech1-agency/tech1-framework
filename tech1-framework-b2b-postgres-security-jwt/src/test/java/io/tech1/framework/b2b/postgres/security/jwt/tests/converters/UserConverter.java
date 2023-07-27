package io.tech1.framework.b2b.postgres.security.jwt.tests.converters;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

// MIGRATE: base-security-jwt
@UtilityClass
public class UserConverter {

    public static List<String> toUsernamesAsStrings0(List<Username> usernames) {
        return usernames.stream()
                .map(Username::identifier)
                .collect(Collectors.toList());
    }

    public static List<String> toUsernamesAsStrings1(List<PostgresDbUser> users) {
        return users.stream()
                .map(user -> user.getUsername().toString())
                .collect(Collectors.toList());
    }
}
