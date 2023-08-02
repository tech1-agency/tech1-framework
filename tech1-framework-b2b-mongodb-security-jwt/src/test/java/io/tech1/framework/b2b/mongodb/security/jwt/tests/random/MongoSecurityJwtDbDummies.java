package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.MongoSecurityJwtDbRandomUtility.*;
import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;

@UtilityClass
public class MongoSecurityJwtDbDummies {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static List<MongoDbInvitationCode> dummyInvitationCodesData1() {
        var invitationCode1 = invitationCode("user1");
        var invitationCode2 = invitationCode("user1");
        var invitationCode3 = invitationCode("user2");
        var invitationCode4 = invitationCode("user2");
        var invitationCode5 = invitationCode("user2");
        var invitationCode6 = invitationCode("user3");

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
    public static List<MongoDbUser> dummyUsersData1() {
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
    public static List<MongoDbUserSession> dummyUserSessionsData1() {
        return List.of(
                MongoSecurityJwtDbRandomUtility.session("sa1"),
                MongoSecurityJwtDbRandomUtility.session("sa1"),
                MongoSecurityJwtDbRandomUtility.session("sa1"),
                MongoSecurityJwtDbRandomUtility.session("user1"),
                MongoSecurityJwtDbRandomUtility.session("user2"),
                MongoSecurityJwtDbRandomUtility.session("admin"),
                MongoSecurityJwtDbRandomUtility.session("admin")
        );
    }

    public static List<MongoDbUserSession> dummyUserSessionsData2() {
        var session1 = MongoSecurityJwtDbRandomUtility.session(TECH1, "token1");
        var session2 = MongoSecurityJwtDbRandomUtility.session(TECH1, "token2");
        var session3 = MongoSecurityJwtDbRandomUtility.session(TECH1, "token3");
        var session4 = MongoSecurityJwtDbRandomUtility.session(Username.of("admin"), "token4");
        return List.of(session1, session2, session3, session4);
    }
}
