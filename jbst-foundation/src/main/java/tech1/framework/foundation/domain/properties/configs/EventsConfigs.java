package tech1.framework.foundation.domain.properties.configs;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.tuples.TuplePercentage;
import tech1.framework.foundation.domain.constants.BigDecimalConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.math.BigDecimal;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EventsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String threadNamePrefix;
    @MandatoryProperty
    private final BigDecimal threadsCorePoolPercentage;
    @MandatoryProperty
    private final BigDecimal threadsMaxPoolPercentage;

    public static EventsConfigs testsHardcoded() {
        return new EventsConfigs("tech1-events", new BigDecimal("75"), BigDecimalConstants.ONE_HUNDRED);
    }

    public static EventsConfigs random() {
        return new EventsConfigs(randomString(), new BigDecimal("25"), BigDecimalConstants.ONE_HUNDRED);
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }

    public TuplePercentage asThreadsCorePoolTuplePercentage() {
        return TuplePercentage.progressTuplePercentage(this.threadsCorePoolPercentage, BigDecimalConstants.ONE_HUNDRED);
    }

    public TuplePercentage asThreadsMaxPoolTuplePercentage() {
        return TuplePercentage.progressTuplePercentage(this.threadsMaxPoolPercentage, BigDecimalConstants.ONE_HUNDRED);
    }
}
