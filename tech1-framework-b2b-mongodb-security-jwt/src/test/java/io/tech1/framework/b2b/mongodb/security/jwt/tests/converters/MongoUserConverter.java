package io.tech1.framework.b2b.mongodb.security.jwt.tests.converters;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.tests.utilities.BaseSecurityJwtJunitUtility.toUsernamesAsStrings0;

@UtilityClass
public class MongoUserConverter {

    public static List<String> toUsernamesAsStrings1(List<MongoDbUser> users) {
        return toUsernamesAsStrings0(users.stream().map(MongoDbUser::getUsername).collect(Collectors.toList()));
    }
}
