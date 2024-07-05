package io.tech1.framework.foundation.domain.converters.columns;

import io.tech1.framework.foundation.domain.base.Version;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
