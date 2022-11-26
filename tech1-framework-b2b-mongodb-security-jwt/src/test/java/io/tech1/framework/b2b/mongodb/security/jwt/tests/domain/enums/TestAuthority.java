package io.tech1.framework.b2b.mongodb.security.jwt.tests.domain.enums;

import io.tech1.framework.domain.base.AbstractAuthority;

public enum TestAuthority implements AbstractAuthority {
    USER("user"),
    ADMIN("admin"),
    INVITATION_CODE_READ("invitationCode:read"),
    INVITATION_CODE_WRITE("invitationCode:write");

    private final String value;

    TestAuthority(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
