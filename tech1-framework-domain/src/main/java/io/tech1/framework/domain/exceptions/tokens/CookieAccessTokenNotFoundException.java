package io.tech1.framework.domain.exceptions.tokens;

public class CookieAccessTokenNotFoundException extends Exception {

    public CookieAccessTokenNotFoundException() {
        super("JWT access token not found");
    }
}
