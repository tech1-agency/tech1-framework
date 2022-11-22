package io.tech1.framework.domain.exceptions.cookie;

public class CookieRefreshTokenInvalidException extends Exception {

    public CookieRefreshTokenInvalidException() {
        super("JWT refresh token is invalid");
    }
}
