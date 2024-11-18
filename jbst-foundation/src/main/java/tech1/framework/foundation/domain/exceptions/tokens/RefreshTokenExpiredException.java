package tech1.framework.foundation.domain.exceptions.tokens;

import tech1.framework.foundation.domain.base.Username;

public class RefreshTokenExpiredException extends Exception {

    public RefreshTokenExpiredException(Username username) {
        super("JWT refresh token is expired. Username: " + username);
    }
}
