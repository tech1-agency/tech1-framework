package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.domain.base.Version;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PostgresVersionConverter implements AttributeConverter<Version, String> {

    @Override
    public String convertToDatabaseColumn(Version version) {
        return version.value();
    }

    @Override
    public Version convertToEntityAttribute(String value) {
        return new Version(value);
    }
}
