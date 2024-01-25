package io.tech1.framework.b2b.base.security.jwt.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class SpringAuthoritiesUtility {

    public static List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Stream<String> authorities) {
        return authorities.map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Set<String> authorities) {
        return getSimpleGrantedAuthorities(authorities.stream());
    }

    public static Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(String... authorities) {
        return getSimpleGrantedAuthorities(Stream.of(authorities));
    }
}
