package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@SuppressWarnings("unused")
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class RecipientsConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final List<String> to;

    public static RecipientsConfigs testsHardcoded() {
        return new RecipientsConfigs(
                List.of(
                        "test1@" + JbstConstants.Domains.HARDCODED,
                        "test2@" + JbstConstants.Domains.HARDCODED
                )
        );
    }
}
