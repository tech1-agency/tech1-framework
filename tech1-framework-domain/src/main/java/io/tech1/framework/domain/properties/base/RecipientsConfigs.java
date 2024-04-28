package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.constants.DomainConstants;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class RecipientsConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final List<String> to;

    public static RecipientsConfigs testsHardcoded() {
        return new RecipientsConfigs(
                List.of(
                        "test1@" + DomainConstants.TECH1,
                        "test2@" + DomainConstants.TECH1
                )
        );
    }
}
