package io.tech1.framework.b2b.postgres.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtDbRandomUtility;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.springframework.util.StringUtils.capitalize;

@UtilityClass
public class PostgresSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static PostgresDbInvitationCode invitationCodeByOwner(String owner) {
        return new PostgresDbInvitationCode(
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
        return randomUserBy(username, List.of(authority));
    }

    public static PostgresDbUser randomUserBy(String username, List<String> authorities) {
        var user = new PostgresDbUser(
                Username.of(username),
                randomPassword(),
                randomZoneId(),
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
    public static PostgresDbUserSession session(String owner) {
        return new PostgresDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner));
    }

    public static PostgresDbUserSession session(Username owner, String accessToken) {
        return new PostgresDbUserSession(BaseSecurityJwtDbRandomUtility.session(owner, accessToken));
    }
}
