package io.tech1.framework.domain.exceptions.tokens;

public class CookieRefreshTokenInvalidException extends Exception {

    public CookieRefreshTokenInvalidException() {
        super("JWT refresh token is invalid");
    }
}
