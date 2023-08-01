package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.util.Objects.nonNull;

@Converter
public class PostgresJwtAccessTokenConverter implements AttributeConverter<JwtAccessToken, String> {

    @Override
    public String convertToDatabaseColumn(JwtAccessToken accessToken) {
        return nonNull(accessToken) ? accessToken.value() : null;
    }

    @Override
    public JwtAccessToken convertToEntityAttribute(String value) {
        return nonNull(value) ? JwtAccessToken.of(value) : null;
    }
}
