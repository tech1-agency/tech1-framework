package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record ObjectId(@NotNull String value) {
    @JsonCreator
    public static ObjectId of(String value) {
        return new ObjectId(value);
    }

    @JsonCreator
    public static ObjectId of(Integer value) {
        return new ObjectId(value.toString());
    }

    @JsonCreator
    public static ObjectId of(Long value) {
        return new ObjectId(value.toString());
    }

    public static ObjectId random() {
        return of(randomString());
    }

    public static ObjectId undefined() {
        return of(UNDEFINED);
    }

    public static ObjectId testsHardcoded() {
        return of("14411F887FF0758B05B3");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
