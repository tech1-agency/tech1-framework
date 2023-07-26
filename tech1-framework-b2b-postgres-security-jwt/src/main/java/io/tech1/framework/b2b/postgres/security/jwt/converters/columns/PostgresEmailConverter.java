package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.domain.base.Email;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PostgresEmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.value();
    }

    @Override
    public Email convertToEntityAttribute(String value) {
        return Email.of(value);
    }
}
