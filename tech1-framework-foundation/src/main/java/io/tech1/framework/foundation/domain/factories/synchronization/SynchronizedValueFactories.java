package io.tech1.framework.foundation.domain.factories.synchronization;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SynchronizedValueFactories {

    public static AtomicLongIncrementalTime2013NonceFactory atomicLongIncrementalTime2013NonceFactory() {
        return new AtomicLongIncrementalTime2013NonceFactory();
    }
}
