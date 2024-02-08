package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.*;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static java.util.Objects.nonNull;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultUsers extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private List<DefaultUser> users;

    public static DefaultUsers testsHardcoded() {
        return new DefaultUsers(true, new ArrayList<>());
    }

    public static DefaultUsers random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static DefaultUsers enabled() {
        return testsHardcoded();
    }

    public static DefaultUsers disabled() {
        return new DefaultUsers(false, new ArrayList<>());
    }

    public final Set<String> getDefaultUsersAuthorities() {
        if (nonNull(this.users)) {
            return this.users.stream().map(DefaultUser::getAuthorities)
                    .filter(Objects::nonNull)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }
}
