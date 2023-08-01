package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;

@UtilityClass
public class MongoSecurityJwtDbRandomUtility {

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
    // TODO [YY] migrate -> base
    public static AnyDbUserSession anyDbUserSession(String owner) {
        return AnyDbUserSession.ofPersisted(
                entity(UserSessionId.class),
                Username.of(owner),
                entity(JwtAccessToken.class),
                entity(JwtRefreshToken.class),
                randomUserRequestMetadata()
        );
    }

    // TODO [YY] migrate -> base
    public static AnyDbUserSession anyDbUserSession(String owner, String accessToken) {
        return AnyDbUserSession.ofPersisted(
                entity(UserSessionId.class),
                Username.of(owner),
                JwtAccessToken.of(accessToken),
                entity(JwtRefreshToken.class),
                randomUserRequestMetadata()
        );
    }

    public static MongoDbUserSession sessionByOwner(String owner) {
        return new MongoDbUserSession(anyDbUserSession(owner));
    }

    public static MongoDbUserSession session(Username owner, String accessToken) {
        return new MongoDbUserSession(anyDbUserSession(owner.identifier(), accessToken));
    }
}
