package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

// Lombok (property-based)
@Data
public class DefaultUsers implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;
    @NonMandatoryProperty
    private List<DefaultUser> users;

    // NOTE: test-purposes
    public static DefaultUsers of(
            boolean enabled,
            List<DefaultUser> users
    ) {
        var instance = new DefaultUsers();
        instance.enabled = enabled;
        instance.users = users;
        return instance;
    }

    // NOTE: test-purposes
    public static DefaultUsers enabled() {
        return of(
                true,
                new ArrayList<>()
        );
    }

    // NOTE: test-purposes
    public static DefaultUsers disabled() {
        return of(
                false,
                new ArrayList<>()
        );
    }

    public final Set<String> getDefaultUsersAuthorities() {
        if (nonNull(this.users)) {
            return this.users.stream().map(DefaultUser::getAuthorities)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }
}
