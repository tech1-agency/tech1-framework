package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;

public abstract class AbstractAttributeConverter<X, Y> implements AttributeConverter<X, Y> {
    protected final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
