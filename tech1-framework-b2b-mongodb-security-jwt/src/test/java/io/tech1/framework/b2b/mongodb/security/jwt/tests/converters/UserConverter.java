package io.tech1.framework.b2b.mongodb.security.jwt.tests.converters;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserConverter {

    public static List<String> toUsernamesAsStrings(List<DbUser> users) {
        return users.stream()
                .map(user -> user.getUsername().toString())
                .collect(Collectors.toList());
    }
}
