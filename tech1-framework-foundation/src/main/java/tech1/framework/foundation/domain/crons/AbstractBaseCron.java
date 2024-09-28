package tech1.framework.foundation.domain.crons;

import tech1.framework.foundation.domain.properties.base.Cron;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractBaseCron {

    public abstract void processException(Exception ex);

    public void executeCron(Cron cron, AbstractCronAction action) {
        this.executeCron(
                cron.isEnabled(),
                action
        );
    }

    public void alwaysExecuteCron(AbstractCronAction action) {
        this.executeCron(
                true,
                action
        );
    }

    // ================================================================================================================
    // PRIVATE METHODS
    // ================================================================================================================
    public void executeCron(boolean enabled, AbstractCronAction action) {
        try {
            if (enabled) {
                action.execute();
            }
        } catch (Exception ex) {
            this.processException(ex);
        }
    }
}
