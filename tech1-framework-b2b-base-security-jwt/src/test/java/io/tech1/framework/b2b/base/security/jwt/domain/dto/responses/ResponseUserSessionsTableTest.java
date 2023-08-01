package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.FLAG_UK;
import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.FLAG_USA;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

class ResponseUserSessionsTableTest {

    @Test
    void noSessionsConstructorTest() {
        // Act
        var actual = ResponseUserSessionsTable.of(new ArrayList<>());

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.sessions()).isEmpty();
        assertThat(actual.anyPresent()).isFalse();
        assertThat(actual.anyProblem()).isFalse();
    }

    @Test
    void constructorTest() {
        // Arrange
        var username = randomUsername();
        var responseUserSession21 = ResponseUserSession2.of(
                entity(UserSessionId.class),
                username,
                new CookieAccessToken(randomString()),
                new JwtAccessToken("token1"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", FLAG_UK, "London"),
                        randomUserAgentDetails()
                )
        );
        var responseUserSession22 = ResponseUserSession2.of(
                entity(UserSessionId.class),
                username,
                new CookieAccessToken("token2"),
                new JwtAccessToken("token2"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "USA", "US", FLAG_USA, "New York"),
                        validUserAgentDetails()
                )
        );
        var responseUserSession23 = ResponseUserSession2.of(
                entity(UserSessionId.class),
                username,
                new CookieAccessToken(randomString()),
                new JwtAccessToken("token3"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "UK", "UK", FLAG_UK, "Liverpool"),
                        invalidUserAgentDetails()
                )
        );

        // Act
        var actual = ResponseUserSessionsTable.of(new ArrayList<>(List.of(responseUserSession21, responseUserSession22, responseUserSession23)));

        // Assert
        assertThat(actual.sessions()).hasSize(3);
        assertThat(actual.sessions().get(0).current()).isTrue();
        assertThat(actual.sessions().get(0).where()).isEqualTo("USA, New York");
        assertThat(actual.sessions().get(1).current()).isFalse();
        assertThat(actual.sessions().get(1).where()).isEqualTo("UK, Liverpool");
        assertThat(actual.sessions().get(2).current()).isFalse();
        assertThat(actual.sessions().get(2).where()).isEqualTo("UK, London");
        assertThat(actual.anyPresent()).isTrue();
        assertThat(actual.anyProblem()).isTrue();
    }
}
