package tech1.framework.iam.tests.random.mongodb;

import lombok.experimental.UtilityClass;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.iam.domain.mongodb.MongoDbInvitationCode;
import tech1.framework.iam.domain.mongodb.MongoDbUser;
import tech1.framework.iam.domain.mongodb.MongoDbUserSession;

import java.util.List;
import java.util.Set;

import static tech1.framework.foundation.domain.base.AbstractAuthority.*;

@UtilityClass
public class MongoSecurityJwtDbDummies {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static List<MongoDbInvitationCode> dummyInvitationCodesData1() {
        var invitationCode1 = MongoDbInvitationCode.admin("user1");
        var invitationCode2 = MongoDbInvitationCode.admin("user1");
        var invitationCode3 = MongoDbInvitationCode.admin("user2");
        var invitationCode4 = MongoDbInvitationCode.admin("user2");
        var invitationCode5 = MongoDbInvitationCode.admin("user2");
        var invitationCode6 = MongoDbInvitationCode.admin("user3");

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

    public static List<MongoDbInvitationCode> dummyInvitationCodesData2() {
        var invitationCode1 = MongoDbInvitationCode.admin("owner22", "value22");
        var invitationCode2 = MongoDbInvitationCode.admin("owner22", "abc");
        var invitationCode3 = MongoDbInvitationCode.admin("owner22", "value44");
        var invitationCode4 = MongoDbInvitationCode.admin("owner11", "value222");
        var invitationCode5 = MongoDbInvitationCode.admin("owner11", "value111");
        var invitationCode6 = MongoDbInvitationCode.admin("owner33", "value123", "invited1");
        var invitationCode7 = MongoDbInvitationCode.admin("owner34", "value234", "invited2");
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
    public static List<MongoDbUser> dummyUsersData1() {
        return List.of(
                MongoDbUser.randomSuperadmin("sa1"),
                MongoDbUser.randomSuperadmin("sa2"),
                MongoDbUser.randomAdmin("admin1"),
                MongoDbUser.random("user1", Set.of("user", INVITATION_CODE_WRITE)),
                MongoDbUser.random("user2", Set.of("user", INVITATION_CODE_READ)),
                MongoDbUser.random("sa3", Set.of(INVITATION_CODE_READ, SUPERADMIN, INVITATION_CODE_WRITE))
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static List<MongoDbUserSession> dummyUserSessionsData1() {
        var session1 = MongoDbUserSession.random(Username.testsHardcoded().value(), "awt1", "rwt1");
        var session2 = MongoDbUserSession.random(Username.testsHardcoded().value(), "awt2", "rwt2");
        var session3 = MongoDbUserSession.random(Username.testsHardcoded().value(), "awt3", "rwt3");
        var session4 = MongoDbUserSession.random(Username.testsHardcoded().value(), "awt4", "rwt4");
        var session5 = MongoDbUserSession.random("user1", "atoken11", "rtoken11");
        var session6 = MongoDbUserSession.random("user1", "atoken12", "rtoken12");
        var session7 = MongoDbUserSession.random("sa", "atoken", "rtoken");
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

    public static List<MongoDbUserSession> dummyUserSessionsData2() {
        var session1 = MongoDbUserSession.random(Username.testsHardcoded(), "token1");
        var session2 = MongoDbUserSession.random(Username.testsHardcoded(), "token2");
        var session3 = MongoDbUserSession.random(Username.testsHardcoded(), "token3");
        var session4 = MongoDbUserSession.random(Username.of("admin"), "token4");
        return List.of(session1, session2, session3, session4);
    }
}
