package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;

@Converter
public class PostgresZoneIdConverter implements AttributeConverter<ZoneId, String> {

    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        return zoneId.getId();
    }

    @Override
    public ZoneId convertToEntityAttribute(String value) {
        return ZoneId.of(value);
    }
}
