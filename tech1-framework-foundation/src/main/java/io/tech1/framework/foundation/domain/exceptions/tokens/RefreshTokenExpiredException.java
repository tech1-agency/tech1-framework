package io.tech1.framework.foundation.domain.exceptions.tokens;

import io.tech1.framework.foundation.domain.base.Username;

public class RefreshTokenExpiredException extends Exception {

    public RefreshTokenExpiredException(Username username) {
        super("JWT refresh token is expired. Username: " + username);
    }
}
