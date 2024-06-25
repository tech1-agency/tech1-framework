package io.tech1.framework.foundation.domain.factories.synchronization;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongIncrementalTime2013NonceFactory implements SynchronizedValueFactory<Long> {

    // Jan 1st, 2013 in milliseconds from epoch
    private static final long START_MILLIS = 1356998400000L;

    // counter for the nonce
    private final AtomicLong lastNonce = new AtomicLong((System.currentTimeMillis() - START_MILLIS) / 250L);

    @Override
    public Long createValue() {
        return this.lastNonce.incrementAndGet();
    }

}
