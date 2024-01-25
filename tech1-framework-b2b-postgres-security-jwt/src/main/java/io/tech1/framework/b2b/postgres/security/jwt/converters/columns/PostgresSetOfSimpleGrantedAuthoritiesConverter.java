package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.constants.StringConstants.SEMICOLON;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class PostgresSetOfSimpleGrantedAuthoritiesConverter implements AttributeConverter<Set<SimpleGrantedAuthority>, String> {

    @Override
    public String convertToDatabaseColumn(Set<SimpleGrantedAuthority> authorities) {
        return isEmpty(authorities) ? null : authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(joining(SEMICOLON));
    }

    @Override
    public Set<SimpleGrantedAuthority> convertToEntityAttribute(String value) {
        return isNull(value) ? new HashSet<>() : getSimpleGrantedAuthorities(Stream.of(value.split(SEMICOLON)));
    }
}
