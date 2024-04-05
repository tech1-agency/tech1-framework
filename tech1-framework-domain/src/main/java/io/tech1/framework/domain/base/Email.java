package io.tech1.framework.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.tech1.framework.domain.constants.DomainConstants;
import org.jetbrains.annotations.NotNull;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.isEmail;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
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
        return of(UNKNOWN + "@" + DomainConstants.TECH1);
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

    private static class ConstraintValidatorOnEmail implements ConstraintValidator<ValidEmail, Email> {
        @Override
        public boolean isValid(Email email, ConstraintValidatorContext constraintValidatorContext) {
            if (isNull(email)) {
                return false;
            }
            if (!hasLength(email.value)) {
                return false;
            }
            return isEmail(email);
        }
    }
}
