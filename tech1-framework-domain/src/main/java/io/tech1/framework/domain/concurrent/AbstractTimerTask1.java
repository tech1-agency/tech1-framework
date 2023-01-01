package io.tech1.framework.domain.concurrent;

import io.tech1.framework.domain.time.SchedulerConfiguration;
import io.tech1.framework.domain.time.TimeAmount;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public abstract class AbstractTimerTask1 {
    public static final TimeAmount DURATION_FOREVER = TimeAmount.of(1L, ChronoUnit.FOREVER);

    @Getter
    protected volatile TimerTaskState state;

    private final SchedulerConfiguration interval;
    private final TimeAmount duration;

    @Getter
    private long elapsedTime;

    private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
    private Future<?> scheduledFuture = null;

    protected AbstractTimerTask1(
            SchedulerConfiguration interval
    ) {
        this.interval = interval;
        this.duration = DURATION_FOREVER;
        this.elapsedTime = 0;
    }

    protected AbstractTimerTask1(
            SchedulerConfiguration interval,
            TimeAmount duration
    ) {
        this.interval = interval;
        this.duration = duration;
        this.elapsedTime = 0;
    }

    public abstract void onTick();

    protected void onComplete() {
        // ignored by default
        // override on complete on demand
    }

    public final void switchState() {
        if (this.state.isOperative()) {
            this.stop();
        } else {
            this.start();
        }
    }

    public void start() {
        if (this.state.isOperative()) {
            return;
        }
        this.state = TimerTaskState.OPERATIVE;
        this.scheduledFuture = this.scheduledExecutorService.scheduleWithFixedDelay(() -> {
            this.onTick();
            this.elapsedTime += this.interval.getDelayedSeconds();
            if (this.duration.toSeconds() > 0 && this.elapsedTime >= this.duration.toSeconds()) {
                this.onComplete();
                this.scheduledFuture.cancel(false);
            }
        }, this.interval.getInitialDelay(), this.interval.getDelay(), this.interval.getUnit());
    }

    public void stop() {
        if (!this.state.isOperative()) {
            return;
        }
        this.scheduledFuture.cancel(false);
        this.state = TimerTaskState.STOPPED;
        this.elapsedTime = 0;
    }

    public final long getRemainingTime() {
        if (ChronoUnit.FOREVER.equals(this.duration.getUnit())) {
            return AbstractTimerTask1.DURATION_FOREVER.toSeconds();
        } else {
            return this.duration.toSeconds() - this.elapsedTime;
        }
    }
}
