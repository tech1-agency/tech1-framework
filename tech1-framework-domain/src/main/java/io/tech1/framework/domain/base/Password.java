package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.containsCamelCaseLettersAndNumbersWithLength;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record Password(@NotNull String value) {
    @JsonCreator
    public static Password of(String value) {
        return new Password(value);
    }

    public static Password random() {
        return of(randomString());
    }

    public static Password testsHardcoded() {
        return of("PasswordTH/Tech1");
    }

    public void assertContainsCamelCaseLettersAndNumbersWithLengthOrThrow(int length) {
        if (!containsCamelCaseLettersAndNumbersWithLength(this.value, 8)) {
            throw new IllegalArgumentException("New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least %s characters long".formatted(length));
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
