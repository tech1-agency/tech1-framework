package io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
public abstract class AbstractRunner {

    @BeforeEach
    public void beforeEach() throws Exception {
        // todo yy
//        addConstructorRule(DbUser.class, clazz -> new DbUser(randomUsername(), randomString(), randomZoneId().getId(), List.of()));
//        addConstructorRule(
//                DbUserSession.class,
//                clazz ->
//                        new DbUserSession(
//                                new JwtRefreshToken(randomString()),
//                                randomUsername(),
//                                entity(UserRequestMetadata.class)
//                        )
//        );
    }
}
