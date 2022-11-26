package io.tech1.framework.domain.exceptions.cookie;

public class CookieRefreshTokenNotFoundException extends Exception {

    public CookieRefreshTokenNotFoundException() {
        super("JWT refresh token not found");
    }
}
