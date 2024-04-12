package io.tech1.framework.domain.concurrent;

import io.tech1.framework.domain.time.SchedulerConfiguration;
import io.tech1.framework.domain.time.TimeAmount;

public abstract class AbstractInfiniteTimerTask extends AbstractTimerTask {

    protected AbstractInfiniteTimerTask(
            SchedulerConfiguration interval
    ) {
        super(
                interval,
                TimeAmount.forever()
        );
    }

    @Override
    public void onComplete() {
        // ignored on infinite timer task
    }
}
