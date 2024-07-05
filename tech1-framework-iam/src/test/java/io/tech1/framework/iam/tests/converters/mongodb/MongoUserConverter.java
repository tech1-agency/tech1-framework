package io.tech1.framework.iam.tests.converters.mongodb;

import io.tech1.framework.iam.domain.mongodb.MongoDbUser;
import io.tech1.framework.iam.domain.mongodb.MongoDbUserSession;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.iam.tests.utilities.BaseSecurityJwtJunitUtility.toUsernamesAsStrings0;

@UtilityClass
public class MongoUserConverter {

    public static List<String> toUsernamesAsStrings1(List<MongoDbUser> users) {
        return toUsernamesAsStrings0(users.stream().map(MongoDbUser::getUsername).collect(Collectors.toList()));
    }

    public static List<String> toUsernamesAsStrings2(List<MongoDbUserSession> sessions) {
        return toUsernamesAsStrings0(sessions.stream().map(MongoDbUserSession::getUsername).collect(Collectors.toList()));
    }

    public static List<String> toAccessTokensAsStrings2(List<MongoDbUserSession> sessions) {
        return sessions.stream().map(session -> session.getAccessToken().value()).collect(Collectors.toList());
    }
}
