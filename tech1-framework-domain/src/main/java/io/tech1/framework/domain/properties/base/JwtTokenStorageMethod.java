package io.tech1.framework.domain.properties.base;

public enum JwtTokenStorageMethod {
    COOKIES,
    HEADERS;

    public boolean isCookies() {
        return COOKIES.equals(this);
    }

    public boolean isHeaders() {
        return HEADERS.equals(this);
    }
}
