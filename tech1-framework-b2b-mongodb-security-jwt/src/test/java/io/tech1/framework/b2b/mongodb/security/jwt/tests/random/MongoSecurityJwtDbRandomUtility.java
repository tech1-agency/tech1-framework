package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtDbRandomUtility;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.authorities;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.springframework.util.StringUtils.capitalize;

@UtilityClass
public class MongoSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static MongoDbInvitationCode invitationCode(String owner) {
        return new MongoDbInvitationCode(Username.of(owner), authorities("admin"));
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
        var user = new MongoDbUser(
                Username.of(username),
                randomPassword(),
                randomZoneId().getId(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        user.setEmail(Email.of(username + "@tech1.io"));
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
}
