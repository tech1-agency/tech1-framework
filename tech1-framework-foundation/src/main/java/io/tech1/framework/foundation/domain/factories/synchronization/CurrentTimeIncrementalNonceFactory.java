package io.tech1.framework.foundation.domain.factories.synchronization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Class computes a current time-based nonce.
 *
 * <p>This class while designed to be thread-safe does not protect against multiple processes where
 * the system clock may be out of sync. It also does not protect a system where a lower granularity
 * time unit like {@link TimeUnit#SECONDS} is used and the same nonce is computed at the same time
 * from competing processes.
 *
 * <p>Compatibility is limited to the time units specified.
 */
public class CurrentTimeIncrementalNonceFactory implements SynchronizedValueFactory<Long> {

    private final AtomicLong nonce = new AtomicLong(0);

    private final Supplier<Long> timeSupplier;

    public CurrentTimeIncrementalNonceFactory(TimeUnit timeUnit) {
        this.timeSupplier = switch (timeUnit) {
            case SECONDS -> () -> System.currentTimeMillis() / 1000;
            case MILLISECONDS -> System::currentTimeMillis;
            case MICROSECONDS -> () -> System.nanoTime() / 1000;
            case NANOSECONDS -> System::nanoTime;
            default -> throw new IllegalArgumentException("TimeUnit %s not supported".formatted(timeUnit));
        };
    }

    @Override
    public Long createValue() {
        return this.nonce.updateAndGet(prevNonce -> {
            long newNonce = this.timeSupplier.get();
            if (newNonce <= prevNonce) {
                newNonce = prevNonce + 1;
            }
            return newNonce;
        });
    }
}
