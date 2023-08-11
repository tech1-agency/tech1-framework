package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;

import static java.util.Objects.nonNull;

@Converter
public class PostgresZoneIdConverter implements AttributeConverter<ZoneId, String> {

    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        return nonNull(zoneId) ? zoneId.getId() : null;
    }

    @Override
    public ZoneId convertToEntityAttribute(String value) {
        return nonNull(value) ? ZoneId.of(value) : null;
    }
}
