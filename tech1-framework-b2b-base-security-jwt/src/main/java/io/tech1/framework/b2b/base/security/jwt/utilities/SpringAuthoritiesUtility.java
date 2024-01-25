package io.tech1.framework.b2b.base.security.jwt.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SpringAuthoritiesUtility {

    public static List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
