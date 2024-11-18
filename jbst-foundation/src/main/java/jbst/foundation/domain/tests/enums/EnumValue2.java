package jbst.foundation.domain.tests.enums;

import jbst.foundation.domain.enums.EnumValue;
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
