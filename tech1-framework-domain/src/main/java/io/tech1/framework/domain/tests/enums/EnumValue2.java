package io.tech1.framework.domain.tests.enums;

import io.tech1.framework.domain.enums.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumValue2 implements EnumValue<String> {
    TECH1("Tech1"),
    FRAMEWORK("Framework"),
    UNKNOWN("Unknown");

    private final String value;

    @Override
    public String getValue() {
        return this.value;
    }
}
