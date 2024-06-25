package io.tech1.framework.foundation.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.foundation.domain.constants.DomainConstants;
import io.tech1.framework.foundation.domain.constants.StringConstants;
import io.tech1.framework.foundation.utilities.http.HttpRequestFieldsUtility;
import org.jetbrains.annotations.NotNull;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasLength;

public record Email(@NotNull String value) {

    @JsonCreator
    public static Email of(String value) {
        return new Email(value);
    }

    public static Email random() {
        return of(randomString() + "@" + DomainConstants.TECH1);
    }

    public static Email unknown() {
        return of(StringConstants.UNKNOWN + "@" + DomainConstants.TECH1);
    }

    public static Email testsHardcoded() {
        return of("tests@" + DomainConstants.TECH1);
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
    @Constraint(validatedBy = ConstraintValidatorOnEmail.class)
    public @interface ValidEmail {
        String message() default "is invalid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class ConstraintValidatorOnEmail implements ConstraintValidator<ValidEmail, Email> {
        @Override
        public boolean isValid(Email email, ConstraintValidatorContext constraintValidatorContext) {
            if (isNull(email)) {
                return false;
            }
            if (!hasLength(email.value)) {
                return false;
            }
            return HttpRequestFieldsUtility.isEmail(email);
        }
    }
}
