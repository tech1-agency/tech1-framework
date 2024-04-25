package io.tech1.framework.b2b.postgres.security.jwt.converters.columns;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.constants.StringConstants.EMPTY;
import static io.tech1.framework.domain.constants.StringConstants.SEMICOLON;
import static java.util.stream.Collectors.joining;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;

@Converter
public class PostgresSetOfSimpleGrantedAuthoritiesConverter implements AttributeConverter<Set<SimpleGrantedAuthority>, String> {

    @Override
    public String convertToDatabaseColumn(Set<SimpleGrantedAuthority> authorities) {
        return !isEmpty(authorities) ? authorities.stream().map(SimpleGrantedAuthority::getAuthority).sorted().collect(joining(SEMICOLON)) : EMPTY;
    }

    @Override
    public Set<SimpleGrantedAuthority> convertToEntityAttribute(String value) {
        return hasLength(value) ? getSimpleGrantedAuthorities(Stream.of(value.split(SEMICOLON))) : new HashSet<>();
    }
}
