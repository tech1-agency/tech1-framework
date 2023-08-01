package io.tech1.framework.b2b.postgres.security.jwt.tests.random;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbRandomUtility.*;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.tests.constants.TestsConstants.TECH1;

@UtilityClass
public class PostgresSecurityJwtDbDummies {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static List<PostgresDbInvitationCode> dummyInvitationCodesData1() {
        var invitationCode1 = invitationCodeByOwner("user1");
        var invitationCode2 = invitationCodeByOwner("user1");
        var invitationCode3 = invitationCodeByOwner("user2");
        var invitationCode4 = invitationCodeByOwner("user2");
        var invitationCode5 = invitationCodeByOwner("user2");
        var invitationCode6 = invitationCodeByOwner("user3");

        invitationCode4.setInvited(Username.of("superadmin"));

        return List.of(
                invitationCode1,
                invitationCode2,
                invitationCode3,
                invitationCode4,
                invitationCode5,
                invitationCode6
        );
    }

    // =================================================================================================================
    // Users
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

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static List<PostgresDbUserSession> dummyUserSessionsData1() {
        return List.of(
                sessionByOwner("sa1"),
                sessionByOwner("sa1"),
                sessionByOwner("sa1"),
                sessionByOwner("user1"),
                sessionByOwner("user2"),
                sessionByOwner("admin"),
                sessionByOwner("admin")
        );
    }

    public static List<PostgresDbUserSession> dummyUserSessionsData2() {
        var session1 = PostgresSecurityJwtDbRandomUtility.session(TECH1, "token1");
        var session2 = PostgresSecurityJwtDbRandomUtility.session(TECH1, "token2");
        var session3 = PostgresSecurityJwtDbRandomUtility.session(TECH1, "token3");
        var session4 = PostgresSecurityJwtDbRandomUtility.session(Username.of("admin"), "token4");
        return List.of(session1, session2, session3, session4);
    }
}
