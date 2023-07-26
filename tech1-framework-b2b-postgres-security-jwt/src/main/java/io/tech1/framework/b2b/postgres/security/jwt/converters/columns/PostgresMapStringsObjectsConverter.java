package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@Converter
public class PostgresMapStringsObjectsConverter implements AttributeConverter<Map<String, Object>, String> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Map<String, Object> authorities) {
        return OBJECT_MAPPER.writeValueAsString(authorities);
    }

    @SneakyThrows
    @Override
    public Map<String, Object> convertToEntityAttribute(String value) {
        var typeReference = new TypeReference<Map<String, Object>>() {};
        return OBJECT_MAPPER.readValue(value, typeReference);
    }
}
