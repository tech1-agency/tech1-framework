package io.tech1.framework.foundation.domain.system.reset_server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.foundation.domain.constants.DatetimeConstants;
import io.tech1.framework.foundation.domain.tuples.TuplePercentage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZoneId;

import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;
import static io.tech1.framework.foundation.utilities.time.LocalDateTimeUtility.convertTimestamp;
import static io.tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;

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

    public static ResetServerStatus random() {
        return new ResetServerStatus(10);
    }

    public ResetServerStatus(long stagesCount) {
        this.state = ResetServerState.READY;
        this.stage = 0L;
        this.stagesCount = stagesCount;
        this.percentage = TuplePercentage.zero();
        this.description = this.state.getValue();
    }

    public boolean isStarted() {
        return this.state.isResetting();
    }

    public void reset() {
        this.state = ResetServerState.RESETTING;
        this.stage = 0L;
        this.percentage = TuplePercentage.zero();
        this.description = this.state.getValue();
    }

    public void nextStage(String description) {
        this.stage++;
        this.percentage = TuplePercentage.progressTuplePercentage(this.stage, this.stagesCount);
        this.description = description;
    }

    public void setFailureDescription(Exception ex) {
        this.description = contactDevelopmentTeam(ex.getMessage());
    }

    public void complete(ZoneId zoneId) {
        this.state = ResetServerState.READY;
        this.stage = this.stagesCount;
        this.percentage = TuplePercentage.progressTuplePercentage(this.stage, this.stagesCount);
        var time = convertTimestamp(getCurrentTimestamp(), zoneId).format(DatetimeConstants.DTF11);
        this.description = "Successfully completed at " + time;
    }
}
