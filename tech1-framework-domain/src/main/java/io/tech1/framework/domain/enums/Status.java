package io.tech1.framework.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Status {
    // Major
    STARTED("Started"),
    COMPLETED("Completed"),
    // Progress
    PROGRESS_25("Progress, 25%"),
    PROGRESS_33("Progress, 33%"),
    PROGRESS_50("Progress, 50%"),
    PROGRESS_66("Progress, 66%"),
    PROGRESS_75("Progress, 75%");

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

    public boolean isStarted() {
        return STARTED.equals(this);
    }

    public boolean isCompleted() {
        return COMPLETED.equals(this);
    }
}
