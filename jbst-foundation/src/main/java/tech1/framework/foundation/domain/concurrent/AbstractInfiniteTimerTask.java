package tech1.framework.foundation.domain.concurrent;

import tech1.framework.foundation.domain.time.SchedulerConfiguration;
import tech1.framework.foundation.domain.time.TimeAmount;

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
