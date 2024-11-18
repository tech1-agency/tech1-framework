package tech1.framework.iam.converters.columns;

import tech1.framework.iam.domain.jwt.JwtAccessToken;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
