package io.tech1.framework.domain.system.reset_server;

import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.system.reset_server.ResetServerState.READY;
import static io.tech1.framework.domain.system.reset_server.ResetServerState.RESETTING;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static io.tech1.framework.domain.tuples.TuplePercentage.progressTuplePercentage;
import static io.tech1.framework.domain.tuples.TuplePercentage.zero;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

class ResetServerStatusTest extends AbstractFolderSerializationRunner {

    @Override
    protected String getFolder() {
        return "system";
    }

    @Test
    void integrationTest() {
        // Act-1
        var status = new ResetServerStatus(9);

        // Assert-1
        assertThat(getGetters(status)).hasSize(6);
        assertThat(status.getState()).isEqualTo(READY);
        assertThat(status.getStage()).isZero();
        assertThat(status.getStagesCount()).isEqualTo(9);
        assertThat(status.getPercentage()).isEqualTo(zero());
        assertThat(status.getDescription()).isEqualTo("Ready");
        assertThat(status.isStarted()).isFalse();
        assertThat(this.writeValueAsString(status)).isEqualTo(readFile(this.getFolder(), "reset-server-status-1.json"));

        // Act-2
        status.reset();

        // Assert-2
        assertThat(status.getState()).isEqualTo(RESETTING);
        assertThat(status.getStage()).isEqualTo(0);
        assertThat(status.getStagesCount()).isEqualTo(9);
        assertThat(status.getPercentage()).isEqualTo(zero());
        assertThat(status.getDescription()).isEqualTo("Resetting");
        assertThat(status.isStarted()).isTrue();
        assertThat(this.writeValueAsString(status)).isEqualTo(readFile(this.getFolder(), "reset-server-status-2.json"));

        // Act-3
        status.nextStage("[Ops] 1st stage");

        // Assert-3
        assertThat(status.getState()).isEqualTo(RESETTING);
        assertThat(status.getStage()).isEqualTo(1);
        assertThat(status.getStagesCount()).isEqualTo(9);
        assertThat(status.getPercentage()).isEqualTo(progressTuplePercentage(1, 9));
        assertThat(status.getDescription()).isEqualTo("[Ops] 1st stage");
        assertThat(status.isStarted()).isTrue();
        assertThat(this.writeValueAsString(status)).isEqualTo(readFile(this.getFolder(), "reset-server-status-3.json"));

        // Act-4
        status.complete();

        // Assert-4
        assertThat(status.getState()).isEqualTo(READY);
        assertThat(status.getStage()).isEqualTo(9);
        assertThat(status.getStagesCount()).isEqualTo(9);
        assertThat(status.getPercentage()).isEqualTo(progressTuplePercentage(9, 9));
        assertThat(status.getDescription()).startsWith("Successfully completed at ");
        assertThat(status.getDescription()).endsWith(" (UTC)");
        assertThat(status.isStarted()).isFalse();

        // Act-5
        status.setFailureDescription(new NullPointerException("Tech1 NPE"));

        // Assert-5
        assertThat(status.getState()).isEqualTo(READY);
        assertThat(status.getStage()).isEqualTo(9);
        assertThat(status.getStagesCount()).isEqualTo(9);
        assertThat(status.getPercentage()).isEqualTo(progressTuplePercentage(9, 9));
        assertThat(status.getDescription()).isEqualTo("Tech1 NPE. Please contact development team");
        assertThat(status.isStarted()).isFalse();
        assertThat(this.writeValueAsString(status)).isEqualTo(readFile(this.getFolder(), "reset-server-status-4.json"));
    }
}
