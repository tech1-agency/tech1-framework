package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.constants.DomainConstants;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record Email(@NotNull String value) {

    @JsonCreator
    public static Email of(String value) {
        return new Email(value);
    }

    public static Email random() {
        return of(randomString() + "@" + DomainConstants.TECH1);
    }

    public static Email unknown() {
        return of(UNKNOWN + "@" + DomainConstants.TECH1);
    }

    public static Email testsHardcoded() {
        return of("tests@" + DomainConstants.TECH1);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
