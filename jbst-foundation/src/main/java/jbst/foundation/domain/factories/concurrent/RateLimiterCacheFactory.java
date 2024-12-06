package jbst.foundation.domain.factories.concurrent;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.concurrent.RateLimiterCache;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class RateLimiterCacheFactory {

    public static RateLimiterCache<Username> executeConfirmEmail() {
        return new RateLimiterCache<>(1, Duration.ofMinutes(1), Duration.ofHours(1));
    }

}
