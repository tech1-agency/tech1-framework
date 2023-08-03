package io.tech1.framework.b2b.base.security.jwt.tests.utilities;

import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class BaseSecurityJwtJunitUtility {

    public static List<String> toUsernamesAsStrings0(Set<Username> usernames) {
        return usernames.stream()
                .map(Username::identifier)
                .collect(Collectors.toList());
    }

    public static List<String> toUsernamesAsStrings0(List<Username> usernames) {
        return usernames.stream()
                .map(Username::identifier)
                .collect(Collectors.toList());
    }
}
