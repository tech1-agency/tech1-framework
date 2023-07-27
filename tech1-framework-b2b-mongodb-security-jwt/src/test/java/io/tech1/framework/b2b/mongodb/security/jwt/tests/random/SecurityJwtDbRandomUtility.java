package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
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
    public static List<DbInvitationCode> dummyInvitationCodesData1() {
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
    public static List<DbUser> dummyUsersData1() {
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
    public static List<DbUserSession> dummyUserSessionsData1() {
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
    public static DbInvitationCode invitationCodeByOwner(String owner) {
        return new DbInvitationCode(
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
    public static DbUser superadmin(String username) {
        return randomUserBy(username, SUPER_ADMIN);
    }

    public static DbUser admin(String username) {
        return randomUserBy(username, "admin");
    }

    public static DbUser user(String username) {
        return randomUserBy(username, "user");
    }

    public static DbUser randomUserBy(String username, String authority) {
        return randomUserBy(username, List.of(authority));
    }

    public static DbUser randomUserBy(String username, List<String> authorities) {
        return new DbUser(
                Username.of(username),
                randomPassword(),
                randomZoneId().getId(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static DbUserSession sessionByOwner(String owner) {
        return new DbUserSession(
                new JwtRefreshToken(randomString()),
                Username.of(owner),
                randomUserRequestMetadata()
        );
    }
}
