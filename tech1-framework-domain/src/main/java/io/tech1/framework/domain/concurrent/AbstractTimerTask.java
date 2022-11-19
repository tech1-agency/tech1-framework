package io.tech1.framework.domain.concurrent;

import io.tech1.framework.domain.time.SchedulerConfiguration;
import io.tech1.framework.domain.time.TimeAmount;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public abstract class AbstractTimerTask {
    public static final TimeAmount DURATION_FOREVER = TimeAmount.of(1L, ChronoUnit.FOREVER);

    @Getter
    private volatile boolean isRunning = false;

    private final SchedulerConfiguration interval;
    private final TimeAmount duration;

    @Getter
    private long elapsedTime;

    private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
    private Future<?> scheduledFuture = null;

    protected AbstractTimerTask(
            SchedulerConfiguration interval
    ) {
        this.interval = interval;
        this.duration = DURATION_FOREVER;
        this.elapsedTime = 0;
    }

    protected AbstractTimerTask(
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

    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.scheduledFuture = this.scheduledExecutorService.scheduleWithFixedDelay(() -> {
            onTick();
            this.elapsedTime += this.interval.getDelayedSeconds();
            if (this.duration.toSeconds() > 0 && this.elapsedTime >= this.duration.toSeconds()) {
                onComplete();
                this.scheduledFuture.cancel(false);
            }
        }, this.interval.getInitialDelay(), this.interval.getDelay(), this.interval.getUnit());
    }

    public void stop() {
        pause();
        this.elapsedTime = 0;
    }

    public void pause() {
        if (!this.isRunning) {
            return;
        }
        this.scheduledFuture.cancel(false);
        this.isRunning = false;
    }

    public void resume() {
        this.start();
    }

    public long getRemainingTime() {
        if (ChronoUnit.FOREVER.equals(this.duration.getUnit())) {
            return AbstractTimerTask.DURATION_FOREVER.toSeconds();
        } else {
            return this.duration.toSeconds() - elapsedTime;
        }
    }
}
