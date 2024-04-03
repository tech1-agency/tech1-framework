package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLongGreaterThanZero;

public record Timestamp(long value) {

    @JsonCreator
    public static Timestamp of(long value) {
        return new Timestamp(value);
    }

    public static Timestamp random() {
        return of(randomLongGreaterThanZero());
    }

    public static Timestamp unknown() {
        return of(-1L);
    }

    // Wednesday, April 3, 2024 10:36:10 AM
    public static Timestamp testsHardcoded() {
        return of(1712140570L);
    }

    @JsonValue
    public long getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
