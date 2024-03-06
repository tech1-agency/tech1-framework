package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record ServerName(@NotNull String value) {

    @JsonCreator
    public static ServerName of(String value) {
        return new ServerName(value);
    }

    public static ServerName random() {
        return of(randomString());
    }

    public static ServerName testsHardcoded() {
        return of("tech1-server");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
