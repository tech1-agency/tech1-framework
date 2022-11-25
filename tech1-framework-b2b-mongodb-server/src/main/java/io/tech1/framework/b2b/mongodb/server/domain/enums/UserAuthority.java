package io.tech1.framework.b2b.mongodb.server.domain.enums;

import io.tech1.framework.domain.base.AbstractAuthority;

public enum UserAuthority implements AbstractAuthority {
    USER("user"),
    ADMIN("admin");

    private final String value;

    UserAuthority(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
