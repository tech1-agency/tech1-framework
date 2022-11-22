package io.tech1.framework.domain.exceptions.cookie;

public class CookieAccessTokenNotFoundException extends Exception {

    public CookieAccessTokenNotFoundException() {
        super("JWT access token not found");
    }
}
