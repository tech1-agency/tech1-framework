package io.tech1.framework.iam.tests.random.mongodb;

import io.tech1.framework.iam.domain.mongodb.MongoDbInvitationCode;
import io.tech1.framework.iam.domain.mongodb.MongoDbUser;
import io.tech1.framework.iam.domain.mongodb.MongoDbUserSession;
import io.tech1.framework.foundation.domain.base.Email;
import io.tech1.framework.foundation.domain.base.Password;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.constants.DomainConstants;
import io.tech1.framework.iam.tests.random.BaseSecurityJwtDbRandomUtility;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

import static io.tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.foundation.domain.base.AbstractAuthority.SUPERADMIN;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.*;
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
