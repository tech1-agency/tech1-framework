package tech1.framework.iam.tests.random.postgres;

import lombok.experimental.UtilityClass;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.iam.domain.postgres.db.PostgresDbInvitationCode;
import tech1.framework.iam.domain.postgres.db.PostgresDbUser;
import tech1.framework.iam.domain.postgres.db.PostgresDbUserSession;

import java.util.List;
import java.util.Set;

import static tech1.framework.foundation.domain.base.AbstractAuthority.*;

@UtilityClass
public class PostgresSecurityJwtDbDummies {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static List<PostgresDbInvitationCode> dummyInvitationCodesData1() {
        var invitationCode1 = PostgresDbInvitationCode.admin("user1");
        var invitationCode2 = PostgresDbInvitationCode.admin("user1");
        var invitationCode3 = PostgresDbInvitationCode.admin("user2");
        var invitationCode4 = PostgresDbInvitationCode.admin("user2");
        var invitationCode5 = PostgresDbInvitationCode.admin("user2");
        var invitationCode6 = PostgresDbInvitationCode.admin("user3");

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
        var invitationCode1 = PostgresDbInvitationCode.admin("owner22", "value22");
        var invitationCode2 = PostgresDbInvitationCode.admin("owner22", "abc");
        var invitationCode3 = PostgresDbInvitationCode.admin("owner22", "value44");
        var invitationCode4 = PostgresDbInvitationCode.admin("owner11", "value222");
        var invitationCode5 = PostgresDbInvitationCode.admin("owner11", "value111");
        var invitationCode6 = PostgresDbInvitationCode.admin("owner33", "value123", "invited1");
        var invitationCode7 = PostgresDbInvitationCode.admin("owner34", "value234", "invited2");
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
                PostgresDbUser.randomSuperadmin("sa1"),
                PostgresDbUser.randomSuperadmin("sa2"),
                PostgresDbUser.randomAdmin("admin1"),
                PostgresDbUser.random("user1", Set.of("user", INVITATION_CODE_WRITE)),
                PostgresDbUser.random("user2", Set.of("user", INVITATION_CODE_READ)),
                PostgresDbUser.random("sa3", Set.of(INVITATION_CODE_READ, SUPERADMIN, INVITATION_CODE_WRITE))
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static List<PostgresDbUserSession> dummyUserSessionsData1() {
        var session1 = PostgresDbUserSession.random(Username.testsHardcoded().value(), "awt1", "rwt1");
        var session2 = PostgresDbUserSession.random(Username.testsHardcoded().value(), "awt2", "rwt2");
        var session3 = PostgresDbUserSession.random(Username.testsHardcoded().value(), "awt3", "rwt3");
        var session4 = PostgresDbUserSession.random(Username.testsHardcoded().value(), "awt4", "rwt4");
        var session5 = PostgresDbUserSession.random("user1", "atoken11", "rtoken11");
        var session6 = PostgresDbUserSession.random("user1", "atoken12", "rtoken12");
        var session7 = PostgresDbUserSession.random("sa", "atoken", "rtoken");
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

    public static List<PostgresDbUserSession> dummyUserSessionsData2() {
        var session1 = PostgresDbUserSession.random(Username.testsHardcoded(), "token1");
        var session2 = PostgresDbUserSession.random(Username.testsHardcoded(), "token2");
        var session3 = PostgresDbUserSession.random(Username.testsHardcoded(), "token3");
        var session4 = PostgresDbUserSession.random(Username.of("admin"), "token4");
        return List.of(session1, session2, session3, session4);
    }
}
