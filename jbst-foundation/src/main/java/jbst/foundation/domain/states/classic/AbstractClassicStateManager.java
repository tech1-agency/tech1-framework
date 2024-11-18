package jbst.foundation.domain.states.classic;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractClassicStateManager {
    private ClassicState state;

    protected AbstractClassicStateManager() {
        this.state = ClassicState.CREATED;
    }

    protected AbstractClassicStateManager(ClassicState state) {
        this.state = state;
    }

    // ================================================================================================================
    // States: Abstract
    // ================================================================================================================
    public abstract String getLogKeyword();
    public abstract String getLogId();

    // ================================================================================================================
    // States: Mutation
    // ================================================================================================================
    public final void setState(ClassicState state) {
        LOGGER.info(this.getLogKeyword(), this.getLogId(), this.state, state);
        this.state = state;
    }

    public void start() {
        this.setState(ClassicState.STARTING);
    }

    public void onActivation() {
        this.setState(ClassicState.ACTIVE);
    }

    public void pause() {
        this.setState(ClassicState.PAUSING);
    }

    public void onPaused() {
        this.setState(ClassicState.PAUSED);
    }

    public void stop() {
        this.setState(ClassicState.STOPPING);
    }

    public void onTermination() {
        this.setState(ClassicState.TERMINATED);
    }

    public void complete() {
        this.setState(ClassicState.COMPLETING);
    }

    public void onComplete() {
        this.setState(ClassicState.COMPLETED);
    }
}
