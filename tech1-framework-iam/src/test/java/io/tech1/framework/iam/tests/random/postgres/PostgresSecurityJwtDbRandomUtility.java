package io.tech1.framework.iam.tests.random.postgres;

import io.tech1.framework.iam.domain.postgres.db.PostgresDbInvitationCode;
import io.tech1.framework.iam.domain.postgres.db.PostgresDbUser;
import io.tech1.framework.iam.domain.postgres.db.PostgresDbUserSession;
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
        return randomUserBy(username, SUPERADMIN);
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
