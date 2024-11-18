package jbst.iam.converters.columns;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static tech1.framework.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static tech1.framework.foundation.domain.constants.StringConstants.EMPTY;
import static tech1.framework.foundation.domain.constants.StringConstants.SEMICOLON;

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
