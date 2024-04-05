package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLongGreaterThanZero;
import static java.util.Objects.nonNull;

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

    @Target({
            ElementType.FIELD,
            ElementType.METHOD
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ConstraintValidatorOnTimestamp.class)
    public @interface ValidTimestamp {
        String message() default "is invalid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class ConstraintValidatorOnTimestamp implements ConstraintValidator<ValidTimestamp, Timestamp> {
        @Override
        public boolean isValid(Timestamp timestamp, ConstraintValidatorContext constraintValidatorContext) {
            return nonNull(timestamp) && timestamp.value > 0;
        }
    }
}
