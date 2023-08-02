package io.tech1.framework.b2b.postgres.security.jwt.tests.random;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.tech1.framework.b2b.postgres.security.jwt.tests.random.PostgresSecurityJwtDbRandomUtility.*;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;

@UtilityClass
public class PostgresSecurityJwtDbDummies {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static List<PostgresDbInvitationCode> dummyInvitationCodesData1() {
        var invitationCode1 = PostgresSecurityJwtDbRandomUtility.invitationCode("user1");
        var invitationCode2 = PostgresSecurityJwtDbRandomUtility.invitationCode("user1");
        var invitationCode3 = PostgresSecurityJwtDbRandomUtility.invitationCode("user2");
        var invitationCode4 = PostgresSecurityJwtDbRandomUtility.invitationCode("user2");
        var invitationCode5 = PostgresSecurityJwtDbRandomUtility.invitationCode("user2");
        var invitationCode6 = PostgresSecurityJwtDbRandomUtility.invitationCode("user3");

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

    public static List<PostgresDbInvitationCode> dummyInvitationCodesData2() {
        var invitationCode1 = invitationCode("owner22", "value22");
        var invitationCode2 = invitationCode("owner22", "abc");
        var invitationCode3 = invitationCode("owner22", "value44");
        var invitationCode4 = invitationCode("owner11", "value222");
        var invitationCode5 = invitationCode("owner11", "value111");
        var invitationCode6 = invitationCode("owner33", "value123", "invited1");
        var invitationCode7 = invitationCode("owner34", "value234", "invited2");
        return List.of(
                invitationCode1,
                invitationCode2,
                invitationCode3,
                invitationCode4,
                invitationCode5,
                invitationCode6,
                invitationCode7
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
                session("sa1"),
                session("sa1"),
                session("sa1"),
                session("user1"),
                session("user2"),
                session("admin"),
                session("admin")
        );
    }

    public static List<PostgresDbUserSession> dummyUserSessionsData2() {
        var session1 = session(TECH1, "token1");
        var session2 = session(TECH1, "token2");
        var session3 = session(TECH1, "token3");
        var session4 = session(Username.of("admin"), "token4");
        return List.of(session1, session2, session3, session4);
    }

    public static List<PostgresDbUserSession> dummyUserSessionsData3() {
        var session1 = session(TECH1.identifier(), "awt1", "rwt1");
        var session2 = session(TECH1.identifier(), "awt2", "rwt2");
        var session3 = session(TECH1.identifier(), "awt3", "rwt3");
        var session4 = session(TECH1.identifier(), "awt4", "rwt4");
        var session5 = session("user1", "atoken11", "rtoken11");
        var session6 = session("user1", "atoken12", "rtoken12");
        var session7 = session("sa", "atoken", "rtoken");
        return List.of(
                session1,
                session2,
                session3,
                session4,
                session5,
                session6,
                session7
        );
    }
}
