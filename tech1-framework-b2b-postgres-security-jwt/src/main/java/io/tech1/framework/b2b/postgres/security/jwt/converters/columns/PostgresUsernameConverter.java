package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.domain.base.Username;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.util.Objects.nonNull;

@Converter
public class PostgresUsernameConverter implements AttributeConverter<Username, String> {

    @Override
    public String convertToDatabaseColumn(Username username) {
        return nonNull(username) ? username.value() : null;
    }

    @Override
    public Username convertToEntityAttribute(String value) {
        return nonNull(value) ? Username.of(value) : null;
    }
}
