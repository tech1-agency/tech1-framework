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
    // MAIN

    STARTED("STARTED"),
    COMPLETED("COMPLETED"),

    FAILURE("FAILURE"),
    SUCCESS("SUCCESS"),

    // PROGRESS

    PROGRESS_20("PROGRESS: 20%"),
    PROGRESS_25("PROGRESS: 25%"),
    PROGRESS_33("PROGRESS: 33%"),
    PROGRESS_40("PROGRESS: 40%"),
    PROGRESS_50("PROGRESS: 50%"),
    PROGRESS_60("PROGRESS:, 60%"),
    PROGRESS_66("PROGRESS: 66%"),
    PROGRESS_75("PROGRESS: 75%"),
    PROGRESS_80("PROGRESS: 80%");

    private static final Map<Status, AnsiFormat> MAPPINGS = Map.ofEntries(
            // MAIN
            Map.entry(STARTED, JbstConstants.JColor.BLUE_BOLD_TEXT),
            Map.entry(COMPLETED, JbstConstants.JColor.GREEN_BOLD_TEXT),
            Map.entry(FAILURE, JbstConstants.JColor.RED_BOLD_TEXT),
            Map.entry(SUCCESS, JbstConstants.JColor.GREEN_BOLD_TEXT),
            // PROGRESS
            Map.entry(PROGRESS_20, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_25, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_33, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_40, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_50, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_60, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_66, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_75, JbstConstants.JColor.YELLOW_BOLD_TEXT),
            Map.entry(PROGRESS_80, JbstConstants.JColor.YELLOW_BOLD_TEXT)
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
