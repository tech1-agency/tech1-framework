package io.tech1.framework.iam.server.base.domain.enums;

import tech1.framework.foundation.domain.base.AbstractAuthority;

public enum UserAuthority implements AbstractAuthority {
    USER("user"),
    ADMIN("admin");

    private final String value;

    UserAuthority(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
