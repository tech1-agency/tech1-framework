package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.containsCamelCaseLettersAndNumbersWithLength;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
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

    public static Password testsHardcoded() {
        return of("PasswordTH/Tech1");
    }

    public void assertEqualsOrThrow(Password password) {
        if (!this.equals(password)) {
            throw new IllegalArgumentException("Passwords must be same");
        }
    }

    public void assertContainsCamelCaseLettersAndNumbersWithLengthOrThrow(int length) {
        if (!containsCamelCaseLettersAndNumbersWithLength(this.value, length)) {
            throw new IllegalArgumentException("New password should contain an uppercase latin letter, a lowercase latin letter, a number and be at least %s characters long".formatted(length));
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

    private static class ConstraintValidatorOnPasswordNotBlank implements ConstraintValidator<ValidPasswordNotBlank, Password> {
        @Override
        public boolean isValid(Password password, ConstraintValidatorContext constraintValidatorContext) {
            return nonNull(password) && hasLength(password.value);
        }
    }
}
