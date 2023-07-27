package io.tech1.framework.b2b.postgres.security.jwt.tests.random;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbRandomUtility.*;
import static io.tech1.framework.domain.base.AbstractAuthority.*;

@UtilityClass
public class PostgresSecurityJwtDummies {

    // =================================================================================================================
    // Users: Dummy Data
    // =================================================================================================================
    public static List<PostgresDbUser> dummyUsersData1() {
        return List.of(
                superadmin("sa1"),
                superadmin("sa2"),
                admin("admin"),
                randomUserBy("user1", List.of("user", INVITATION_CODE_WRITE)),
                randomUserBy("user2", List.of("user", INVITATION_CODE_READ)),
                randomUserBy("sa3", List.of(INVITATION_CODE_READ, SUPER_ADMIN, INVITATION_CODE_WRITE))
        );
    }
}
