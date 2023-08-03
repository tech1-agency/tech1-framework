package io.tech1.framework.domain.system.reset_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.domain.constants.DatetimeConstants;
import io.tech1.framework.domain.tuples.TuplePercentage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZoneOffset;

import static io.tech1.framework.domain.tuples.TuplePercentage.progressTuplePercentage;
import static io.tech1.framework.domain.tuples.TuplePercentage.zero;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;
import static io.tech1.framework.domain.utilities.time.LocalDateTimeUtility.convertTimestamp;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ResetServerStatus {

    @JsonIgnore
    private ResetServerState state;
    @JsonIgnore
    private long stage;
    @JsonIgnore
    private final long stagesCount;

    private TuplePercentage percentage;
    private String description;

    public ResetServerStatus(long stagesCount) {
        this.state = ResetServerState.READY;
        this.stage = 0L;
        this.stagesCount = stagesCount;
        this.percentage = zero();
        this.description = this.state.getValue();
    }

    public boolean isStarted() {
        return this.state.isResetting();
    }

    public void reset() {
        this.state = ResetServerState.RESETTING;
        this.stage = 0L;
        this.percentage = zero();
        this.description = this.state.getValue();
    }

    public void nextStage(String description) {
        this.stage++;
        this.percentage = progressTuplePercentage(this.stage, this.stagesCount);
        this.description = description;
    }

    public void setFailureDescription(Exception ex) {
        this.description = contactDevelopmentTeam(ex.getMessage());
    }

    public void complete() {
        this.state = ResetServerState.READY;
        this.stage = this.stagesCount;
        this.percentage = progressTuplePercentage(this.stage, this.stagesCount);
        var time = convertTimestamp(getCurrentTimestamp(), ZoneOffset.UTC).format(DatetimeConstants.DTF1) + " (UTC)";
        this.description = "Successfully completed at " + time;
    }
}
