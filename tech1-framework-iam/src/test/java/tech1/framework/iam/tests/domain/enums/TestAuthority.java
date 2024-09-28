package tech1.framework.iam.tests.domain.enums;

import tech1.framework.foundation.domain.base.AbstractAuthority;

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
