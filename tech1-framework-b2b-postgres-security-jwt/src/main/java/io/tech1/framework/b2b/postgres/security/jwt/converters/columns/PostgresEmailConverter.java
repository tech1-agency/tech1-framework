package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.foundation.domain.base.Email;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static java.util.Objects.nonNull;

@Converter
public class PostgresEmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return nonNull(email) ? email.value() : null;
    }

    @Override
    public Email convertToEntityAttribute(String value) {
        return nonNull(value) ? Email.of(value) : null;
    }
}
