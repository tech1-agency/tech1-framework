package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtDbRandomUtility;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.DomainConstants;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPERADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.springframework.util.StringUtils.capitalize;

@UtilityClass
public class MongoSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static MongoDbInvitationCode invitationCode(String owner) {
        return new MongoDbInvitationCode(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static MongoDbInvitationCode invitationCode(String owner, String value) {
        var invitationCode = invitationCode(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static MongoDbInvitationCode invitationCode(String owner, String value, String invited) {
        var invitationCode = invitationCode(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
    }

    // =================================================================================================================
    // Users
    // =================================================================================================================
    public static MongoDbUser superadmin(String username) {
        return randomUserBy(username, SUPERADMIN);
    }

    public static MongoDbUser admin(String username) {
        return randomUserBy(username, "admin");
    }

    public static MongoDbUser user(String username) {
        return randomUserBy(username, "user");
    }

    public static MongoDbUser randomUserBy(String username, String authority) {
        return randomUserBy(username, Set.of(authority));
    }

    public static MongoDbUser randomUserBy(String username, Set<String> authorities) {
        var user = new MongoDbUser(
                Username.of(username),
                Password.random(),
                randomZoneId(),
                getSimpleGrantedAuthorities(authorities),
                randomBoolean()
        );
        user.setEmail(Email.of(username + "@" + DomainConstants.TECH1));
        user.setName(capitalize(randomString()) + " " + capitalize(randomString()));
        user.setAttributes(
                Map.of(
                        randomString(), randomString(),
                        randomString(), randomLong()
                )
        );
        return user;
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static MongoDbUserSession session(String owner) {
        return new MongoDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner));
    }

    public static MongoDbUserSession session(Username owner, String accessToken) {
        return new MongoDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner, accessToken));
    }

    public static MongoDbUserSession session(String owner, String accessToken, String refreshToken) {
        return new MongoDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner, accessToken, refreshToken));
    }
}
