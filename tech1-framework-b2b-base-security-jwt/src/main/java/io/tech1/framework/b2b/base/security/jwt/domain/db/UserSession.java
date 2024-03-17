package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;

public record UserSession(
        boolean persisted,
        UserSessionId id,
        long createdAt,
        long updatedAt,
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken,
        UserRequestMetadata metadata,
        boolean metadataRenewCron,
        boolean metadataRenewManually
) {

    public static UserSession randomPersistedSession() {
        return UserSession.ofPersisted(
                UserSessionId.random(),
                getCurrentTimestamp(),
                getCurrentTimestamp(),
                Username.random(),
                JwtAccessToken.random(),
                JwtRefreshToken.random(),
                UserRequestMetadata.random(),
                randomBoolean(),
                randomBoolean()
        );
    }

    public static UserSession ofPersisted(
            UserSessionId id,
            long createdAt,
            long updatedAt,
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata,
            boolean metadataRenewCron,
            boolean metadataRenewManually
    ) {
        return new UserSession(
                true,
                id,
                createdAt,
                updatedAt,
                username,
                accessToken,
                refreshToken,
                metadata,
                metadataRenewCron,
                metadataRenewManually
        );
    }

    public static UserSession ofNotPersisted(
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata
    ) {
        var currentTimestamp = getCurrentTimestamp();
        return new UserSession(
                false,
                UserSessionId.undefined(),
                currentTimestamp,
                currentTimestamp,
                username,
                accessToken,
                refreshToken,
                metadata,
                false,
                false
        );
    }

    public boolean isRenewRequired() {
        return this.metadataRenewCron || this.metadataRenewManually;
    }
}
