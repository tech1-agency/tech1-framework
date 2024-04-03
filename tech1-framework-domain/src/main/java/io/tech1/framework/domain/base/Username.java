package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record Username(@NotNull String value) {

    @JsonCreator
    public static Username of(String value) {
        return new Username(value);
    }

    public static Username cron() {
        return of("cron");
    }

    public static Username ops() {
        return of("ops");
    }

    public static Username random() {
        return of(randomString());
    }

    public static Username unknown() {
        return of(UNKNOWN);
    }

    public static Username testsHardcoded() {
        return of("tech1");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
