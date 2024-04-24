package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.domain.base.Version;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// TODO [YYL] never used
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
