package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ResponseUserSessionsTableTest {

    @Test
    public void noSessionsConstructorTest() {
        // Act
        var actual = new ResponseUserSessionsTable(new ArrayList<>());

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getSessions()).hasSize(0);
        assertThat(actual.isAnyPresent()).isFalse();
        assertThat(actual.isAnyProblem()).isFalse();
    }

    @Test
    public void constructorTest() {
        // Arrange
        var username = randomUsername();
        var dbUserSession1 = new DbUserSession(
                new JwtRefreshToken("token1"),
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "London"),
                        randomUserAgentDetails()
                )
        );
        var dbUserSession2 = new DbUserSession(
                new JwtRefreshToken("token2"),
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "USA", "New York"),
                        validUserAgentDetails()
                )
        );
        var dbUserSession3 = new DbUserSession(
                new JwtRefreshToken("token3"),
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "UK", "Liverpool"),
                        invalidUserAgentDetails()
                )
        );
        var responseUserSession21 = new ResponseUserSession2(dbUserSession1, new CookieRefreshToken(randomString()));
        var responseUserSession22 = new ResponseUserSession2(dbUserSession2, new CookieRefreshToken("token2"));
        var responseUserSession23 = new ResponseUserSession2(dbUserSession3, new CookieRefreshToken(randomString()));

        // Act
        var actual = new ResponseUserSessionsTable(new ArrayList<>(List.of(responseUserSession21, responseUserSession22, responseUserSession23)));

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getSessions()).hasSize(3);
        assertThat(actual.getSessions().get(0).isCurrent()).isTrue();
        assertThat(actual.getSessions().get(0).getWhere()).isEqualTo("USA, New York");
        assertThat(actual.getSessions().get(1).isCurrent()).isFalse();
        assertThat(actual.getSessions().get(1).getWhere()).isEqualTo("UK, Liverpool");
        assertThat(actual.getSessions().get(2).isCurrent()).isFalse();
        assertThat(actual.getSessions().get(2).getWhere()).isEqualTo("UK, London");
        assertThat(actual.isAnyPresent()).isTrue();
        assertThat(actual.isAnyProblem()).isTrue();
    }
}
