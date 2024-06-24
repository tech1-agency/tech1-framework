package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.tuples.TuplePercentage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.math.BigDecimal;

import static io.tech1.framework.domain.constants.BigDecimalConstants.ONE_HUNDRED;
import static io.tech1.framework.domain.tuples.TuplePercentage.progressTuplePercentage;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

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
        return new EventsConfigs("tech1-events", new BigDecimal("75"), ONE_HUNDRED);
    }

    public static EventsConfigs random() {
        return new EventsConfigs(randomString(), new BigDecimal("25"), ONE_HUNDRED);
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }

    public TuplePercentage asThreadsCorePoolTuplePercentage() {
        return progressTuplePercentage(this.threadsCorePoolPercentage, ONE_HUNDRED);
    }

    public TuplePercentage asThreadsMaxPoolTuplePercentage() {
        return progressTuplePercentage(this.threadsMaxPoolPercentage, ONE_HUNDRED);
    }
}
