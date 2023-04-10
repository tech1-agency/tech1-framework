package io.tech1.framework.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Lombok
@AllArgsConstructor
public enum Status {
    // Major
    STARTED("Started"),
    COMPLETED("Completed"),
    // Loading
    FAILURE("Failure"),
    SUCCESS("Success"),
    // Progress
    PROGRESS_20("Progress, 20%"),
    PROGRESS_25("Progress, 25%"),
    PROGRESS_33("Progress, 33%"),
    PROGRESS_40("Progress, 40%"),
    PROGRESS_50("Progress, 50%"),
    PROGRESS_60("Progress, 60%"),
    PROGRESS_66("Progress, 66%"),
    PROGRESS_75("Progress, 75%"),
    PROGRESS_80("Progress, 80%");

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
