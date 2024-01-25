package io.tech1.framework.domain.crons;

@FunctionalInterface
public interface AbstractCronAction {
    void execute() throws Exception;
}
