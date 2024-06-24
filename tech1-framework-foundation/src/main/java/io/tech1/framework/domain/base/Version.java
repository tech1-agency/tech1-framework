package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.tech1.framework.domain.constants.StringConstants.*;
import static io.tech1.framework.domain.constants.StringConstants.HYPHEN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

public record Version(@NotNull String value) {

    @JsonCreator
    public static Version of(String value) {
        return new Version(value);
    }

    public static Version random() {
        return of(randomString());
    }

    public static Version undefined() {
        return of(UNDEFINED);
    }

    public static Version unknown() {
        return of(UNKNOWN);
    }

    public static Version dash() {
        return of(DASH);
    }

    public static Version hyphen() {
        return of(HYPHEN);
    }

    public static Version testsHardcoded() {
        return of("v1.61803398875");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @Target({
            ElementType.FIELD,
            ElementType.METHOD
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ConstraintValidatorOnVersion.class)
    public @interface ValidVersion {
        String message() default "is invalid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class ConstraintValidatorOnVersion implements ConstraintValidator<ValidVersion, Version> {
        @Override
        public boolean isValid(Version version, ConstraintValidatorContext constraintValidatorContext) {
            return nonNull(version) && hasLength(version.value);
        }
    }
}
