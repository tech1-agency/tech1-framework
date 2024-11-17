package tech1.framework.foundation.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

public record AccountId(@NotNull String value) {
    @JsonCreator
    public static AccountId of(String value) {
        return new AccountId(value);
    }

    public static AccountId hardcoded() {
        return of("jbs7vbj97qnk8do2f7mc");
    }

    public static AccountId random() {
        return new AccountId(randomStringLetterOrNumbersOnly(20).toLowerCase());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
