package io.tech1.framework.foundation.domain.tests.domain.enums;

import io.tech1.framework.foundation.domain.base.AbstractAuthority;

// Used in PropertiesAsserterAndPrinterTest (reflection scan process)
public enum TestAuthority implements AbstractAuthority {
    USER("user"),
    ADMIN("admin");

    private final String value;

    TestAuthority(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
