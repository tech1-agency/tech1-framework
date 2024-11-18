package jbst.foundation.domain.enums;

import com.diogonunes.jcolor.AnsiFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.foundation.domain.constants.JbstConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

// Lombok
@AllArgsConstructor
@Getter
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

    private static final Map<Status, AnsiFormat> MAPPINGS = Map.of(
            // Major
            STARTED, JbstConstants.JColor.BLUE_BOLD_TEXT,
            COMPLETED, JbstConstants.JColor.GREEN_BOLD_TEXT,
            // Loading
            FAILURE, JbstConstants.JColor.RED_BOLD_TEXT,
            SUCCESS, JbstConstants.JColor.GREEN_BOLD_TEXT
    );

    public static AnsiFormat getAnsiFormat(Status status) {
        return MAPPINGS.getOrDefault(status, JbstConstants.JColor.BLACK_BOLD_TEXT);
    }

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

    @JsonIgnore
    public String formatAnsi() {
        return getAnsiFormat(this).format(this.getValue());
    }
}
