package jbst.foundation.domain.concurrent;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jbst.foundation.domain.exceptions.base.TooManyRequestsException;

import java.time.Duration;

@SuppressWarnings("UnstableApiUsage")
public class RateLimiter<T> {

    private final LoadingCache<T, com.google.common.util.concurrent.RateLimiter> cache;

    public RateLimiter(
            int requests,
            Duration duration,
            Duration cacheDuration
    ) {
        var permitsPerSecond = calculatePermitsPerSecond(requests, duration);
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheDuration)
                .build(key -> com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond));
    }

    protected static double calculatePermitsPerSecond(int requests, Duration duration) {
        return (double) requests / duration.getSeconds();
    }

    public boolean tryAcquire(T key) {
        return this.cache.get(key).tryAcquire();
    }

    public void acquire(T key) throws TooManyRequestsException {
        if (!this.cache.get(key).tryAcquire()) {
            throw new TooManyRequestsException();
        }
    }

}
