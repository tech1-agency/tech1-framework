package io.tech1.framework.b2b.mongodb.security.jwt.tests.random;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomPassword;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;

@UtilityClass
public class SecurityJwtDbRandomUtility {

    // =================================================================================================================
    // User: Lists
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
    // User
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
}
