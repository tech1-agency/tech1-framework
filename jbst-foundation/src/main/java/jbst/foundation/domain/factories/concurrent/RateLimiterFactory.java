package jbst.foundation.domain.factories.concurrent;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.concurrent.RateLimiter;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class RateLimiterFactory {

    public static RateLimiter<Username> executeEmailConfirmation() {
        return new RateLimiter<>(1, Duration.ofMinutes(1), Duration.ofHours(1));
    }

}
