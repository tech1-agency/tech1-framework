package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;

@UtilityClass
public class SecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes: Dummy Data
    // =================================================================================================================
    public static List<MongoDbInvitationCode> dummyInvitationCodesData1() {
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
    // Users: Dummy Data
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
    // UserSessions: Dummy Data
    // =================================================================================================================
    public static List<MongoDbUserSession> dummyUserSessionsData1() {
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

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static MongoDbInvitationCode invitationCodeByOwner(String owner) {
        return new MongoDbInvitationCode(
                Username.of(owner),
                List.of(
                        new SimpleGrantedAuthority(randomString()),
                        new SimpleGrantedAuthority(randomString()),
                        new SimpleGrantedAuthority(randomString())
                )
        );
    }

    // =================================================================================================================
    // Users
    // =================================================================================================================
    public static MongoDbUser superadmin(String username) {
        return randomUserBy(username, SUPER_ADMIN);
    }

    public static MongoDbUser admin(String username) {
        return randomUserBy(username, "admin");
    }

    public static MongoDbUser user(String username) {
        return randomUserBy(username, "user");
    }

    public static MongoDbUser randomUserBy(String username, String authority) {
        return randomUserBy(username, List.of(authority));
    }

    public static MongoDbUser randomUserBy(String username, List<String> authorities) {
        return new MongoDbUser(
                Username.of(username),
                randomPassword(),
                randomZoneId().getId(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static MongoDbUserSession sessionByOwner(String owner) {
        return new MongoDbUserSession(
                new JwtRefreshToken(randomString()),
                Username.of(owner),
                randomUserRequestMetadata()
        );
    }
}
