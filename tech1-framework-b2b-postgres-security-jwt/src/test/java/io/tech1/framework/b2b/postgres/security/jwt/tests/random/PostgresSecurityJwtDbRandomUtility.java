package io.tech1.framework.b2b.postgres.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtDbRandomUtility;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.DomainConstants;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.springframework.util.StringUtils.capitalize;

@UtilityClass
public class PostgresSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static PostgresDbInvitationCode invitationCode(String owner) {
        return new PostgresDbInvitationCode(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static PostgresDbInvitationCode invitationCode(String owner, String value) {
        var invitationCode = invitationCode(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static PostgresDbInvitationCode invitationCode(String owner, String value, String invited) {
        var invitationCode = invitationCode(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
    }

    // =================================================================================================================
    // Users
    // =================================================================================================================
    public static PostgresDbUser superadmin(String username) {
        return randomUserBy(username, SUPER_ADMIN);
    }

    public static PostgresDbUser admin(String username) {
        return randomUserBy(username, "admin");
    }

    public static PostgresDbUser user(String username) {
        return randomUserBy(username, "user");
    }

    public static PostgresDbUser randomUserBy(String username, String authority) {
        return randomUserBy(username, Set.of(authority));
    }

    public static PostgresDbUser randomUserBy(String username, Set<String> authorities) {
        var user = new PostgresDbUser(
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
    public static PostgresDbUserSession session(String owner) {
        return new PostgresDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner));
    }

    public static PostgresDbUserSession session(Username owner, String accessToken) {
        return new PostgresDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner, accessToken));
    }

    public static PostgresDbUserSession session(String owner, String accessToken, String refreshToken) {
        return new PostgresDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner, accessToken, refreshToken));
    }
}
