package jbst.foundation.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jbst.foundation.domain.constants.StringConstants;
import org.jetbrains.annotations.NotNull;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static jbst.foundation.utilities.http.HttpRequestFieldsUtility.containsCamelCaseLettersAndNumbers;
import static jbst.foundation.utilities.random.RandomUtility.randomString;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

public record Password(@NotNull String value) {
    @JsonCreator
    public static Password of(String value) {
        return new Password(value);
    }

    public static Password random() {
        return of(randomString());
    }

    public static Password dash() {
        return of(StringConstants.DASH);
    }

    public static Password hyphen() {
        return of(StringConstants.HYPHEN);
    }

    public static Password testsHardcoded() {
        return of("PasswordTH/Tech1");
    }

    public void assertEqualsOrThrow(Password password) {
        if (!this.equals(password)) {
            throw new IllegalArgumentException("Passwords must be same");
        }
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
    @Constraint(validatedBy = ConstraintValidatorOnPasswordNotBlank.class)
    public @interface ValidPasswordNotBlank {
        String message() default "must not be blank";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class ConstraintValidatorOnPasswordNotBlank implements ConstraintValidator<ValidPasswordNotBlank, Password> {
        @Override
        public boolean isValid(Password password, ConstraintValidatorContext constraintValidatorContext) {
            return nonNull(password) && hasLength(password.value);
        }
    }

    @Target({
            ElementType.FIELD,
            ElementType.METHOD
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ConstraintValidatorOnPasswordCamelCaseLettersAndNumbers.class)
    public @interface ValidPasswordCamelCaseLettersAndNumbers {
        String message() default "must not be blank, must be between {min} and {max} symbols, must contain: an uppercase latin letter, a lowercase latin letter and a number";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
        int min() default 0;
        int max() default Integer.MAX_VALUE;
    }

    public static class ConstraintValidatorOnPasswordCamelCaseLettersAndNumbers implements ConstraintValidator<ValidPasswordCamelCaseLettersAndNumbers, Password> {
        private int min;
        private int max;

        @Override
        public void initialize(ValidPasswordCamelCaseLettersAndNumbers constraintAnnotation) {
            this.min = constraintAnnotation.min();
            this.max = constraintAnnotation.max();
        }

        @Override
        public boolean isValid(Password password, ConstraintValidatorContext constraintValidatorContext) {
            if (isNull(password)) {
                return false;
            }
            if (!hasLength(password.value)) {
                return false;
            }
            if (password.value.length() < this.min || password.value.length() > this.max) {
                return false;
            }
            return containsCamelCaseLettersAndNumbers(password.value);
        }
    }
}
