package jbst.iam.tests.converters.postgres;

import jbst.iam.domain.postgres.db.PostgresDbUser;
import jbst.iam.domain.postgres.db.PostgresDbUserSession;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import static jbst.iam.tests.utilities.BaseSecurityJwtJunitUtility.toUsernamesAsStrings0;

@UtilityClass
public class PostgresUserConverter {

    public static List<String> toUsernamesAsStrings1(List<PostgresDbUser> users) {
        return toUsernamesAsStrings0(users.stream().map(PostgresDbUser::getUsername).collect(Collectors.toList()));
    }

    public static List<String> toUsernamesAsStrings2(List<PostgresDbUserSession> sessions) {
        return toUsernamesAsStrings0(sessions.stream().map(PostgresDbUserSession::getUsername).collect(Collectors.toList()));
    }

    public static List<String> toAccessTokensAsStrings2(List<PostgresDbUserSession> sessions) {
        return sessions.stream().map(session -> session.getAccessToken().value()).collect(Collectors.toList());
    }
}
