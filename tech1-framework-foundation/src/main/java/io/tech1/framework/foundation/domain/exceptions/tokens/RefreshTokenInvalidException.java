package io.tech1.framework.foundation.domain.exceptions.tokens;

public class RefreshTokenInvalidException extends Exception {

    public RefreshTokenInvalidException() {
        super("JWT refresh token is invalid");
    }
}
