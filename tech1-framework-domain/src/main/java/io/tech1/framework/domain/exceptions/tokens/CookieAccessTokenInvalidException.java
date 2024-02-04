package io.tech1.framework.domain.exceptions.tokens;

public class CookieAccessTokenInvalidException extends Exception {

    public CookieAccessTokenInvalidException() {
        super("JWT access token is invalid");
    }
}
