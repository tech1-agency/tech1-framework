package tech1.framework.foundation.domain.crons;

@FunctionalInterface
public interface AbstractCronAction {
    void execute() throws Exception;
}
