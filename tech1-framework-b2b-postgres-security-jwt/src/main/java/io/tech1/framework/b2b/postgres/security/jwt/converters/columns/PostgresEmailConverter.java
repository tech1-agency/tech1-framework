package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.domain.base.Email;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
