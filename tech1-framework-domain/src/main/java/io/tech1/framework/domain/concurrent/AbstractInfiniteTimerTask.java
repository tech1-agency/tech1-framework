package io.tech1.framework.domain.concurrent;

import io.tech1.framework.domain.time.SchedulerConfiguration;
import io.tech1.framework.domain.time.TimeAmount;

import static java.time.temporal.ChronoUnit.FOREVER;

public abstract class AbstractInfiniteTimerTask extends AbstractTimerTask {

    protected AbstractInfiniteTimerTask(
            SchedulerConfiguration interval
    ) {
        super(
                interval,
                TimeAmount.of(1L, FOREVER)
        );
    }

    @Override
    public void onComplete() {
        // ignored on infinite timer task
    }
}
