package jbst.foundation.domain.tests.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jbst.foundation.domain.enums.EnumValue;
import jbst.foundation.utilities.enums.EnumCreatorUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnumValue1 implements EnumValue<String> {
    TECH1("Tech1"),
    FRAMEWORK("Framework");

    private final String value;

    @JsonCreator
    public static EnumValue1 findBotType(String value) {
        return EnumCreatorUtility.findEnumByValueIgnoreCaseOrThrow(EnumValue1.class, value);
    }

    @JsonValue
    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
