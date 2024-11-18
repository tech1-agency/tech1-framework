package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.constants.BigDecimalConstants;
import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.tuples.TuplePercentage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.math.BigDecimal;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class AsyncConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String threadNamePrefix;
    @MandatoryProperty
    private final BigDecimal threadsCorePoolPercentage;
    @MandatoryProperty
    private final BigDecimal threadsMaxPoolPercentage;

    public static AsyncConfigs testsHardcoded() {
        return new AsyncConfigs("tech1-async", new BigDecimal("25"), BigDecimalConstants.ONE_HUNDRED);
    }

    public static AsyncConfigs random() {
        return new AsyncConfigs(randomString(), new BigDecimal("25"), BigDecimalConstants.ONE_HUNDRED);
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
