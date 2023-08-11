package io.tech1.framework.domain.crons;

// [migrate]: tech1-framework
@FunctionalInterface
public interface AbstractCronAction {
    void execute() throws Exception;
}
