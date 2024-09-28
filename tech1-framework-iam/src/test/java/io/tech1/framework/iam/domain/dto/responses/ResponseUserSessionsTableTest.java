package io.tech1.framework.iam.domain.dto.responses;

import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentDetails;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static tech1.framework.foundation.domain.tests.constants.TestsFlagsConstants.UK;
import static tech1.framework.foundation.domain.tests.constants.TestsFlagsConstants.USA;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;
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
        var username = Username.random();
        var responseUserSession21 = ResponseUserSession2.of(
                UserSessionId.random(),
                getCurrentTimestamp(),
                username,
                new RequestAccessToken(randomString()),
                new JwtAccessToken("token1"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", UK, "London"),
                        UserAgentDetails.random()
                )
        );
        var responseUserSession22 = ResponseUserSession2.of(
                UserSessionId.random(),
                getCurrentTimestamp(),
                username,
                new RequestAccessToken("token2"),
                new JwtAccessToken("token2"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "USA", "US", USA, "New York"),
                        UserAgentDetails.valid()
                )
        );
        var responseUserSession23 = ResponseUserSession2.of(
                UserSessionId.random(),
                getCurrentTimestamp(),
                username,
                new RequestAccessToken(randomString()),
                new JwtAccessToken("token3"),
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("3.3.3.3"), "UK", "UK", UK, "Liverpool"),
                        UserAgentDetails.invalid()
                )
        );

        // Act
        var actual = ResponseUserSessionsTable.of(new ArrayList<>(List.of(responseUserSession21, responseUserSession22, responseUserSession23)));

        // Assert
        assertThat(actual.sessions()).hasSize(3);
        assertThat(actual.sessions().get(0).current()).isTrue();
        assertThat(actual.sessions().get(0).where()).isEqualTo("🇺🇸 USA, New York");
        assertThat(actual.sessions().get(1).current()).isFalse();
        assertThat(actual.sessions().get(1).where()).isEqualTo("🇬🇧 UK, Liverpool");
        assertThat(actual.sessions().get(2).current()).isFalse();
        assertThat(actual.sessions().get(2).where()).isEqualTo("🇬🇧 UK, London");
        assertThat(actual.anyPresent()).isTrue();
        assertThat(actual.anyProblem()).isTrue();
    }
}
