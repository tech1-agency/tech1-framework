package jbst.foundation.domain.converters.columns;

import jbst.foundation.domain.base.Password;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PostgresPasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.value();
    }

    @Override
    public Password convertToEntityAttribute(String value) {
        return Password.of(value);
    }
}
