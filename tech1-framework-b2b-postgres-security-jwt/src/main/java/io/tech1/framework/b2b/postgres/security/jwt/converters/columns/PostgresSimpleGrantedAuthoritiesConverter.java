package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class PostgresSimpleGrantedAuthoritiesConverter implements AttributeConverter<List<SimpleGrantedAuthority>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<SimpleGrantedAuthority> authorities) {
        return isEmpty(authorities) ? "" : authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(joining(SPLIT_CHAR));
    }

    @Override
    public List<SimpleGrantedAuthority> convertToEntityAttribute(String value) {
        return Objects.isNull(value) ? new ArrayList<>() : Stream.of(value.split(SPLIT_CHAR)).map(SimpleGrantedAuthority::new).toList();
    }
}
