package jbst.iam.tests.domain.enums;

import tech1.framework.foundation.domain.base.AbstractAuthority;

public enum TestAuthority implements AbstractAuthority {
    USER("user"),
    ADMIN("admin"),
    INVITATIONS_READ("invitations:read"),
    INVITATIONS_WRITE("invitations:write");

    private final String value;

    TestAuthority(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
