package jbst.foundation.domain.crons;

@FunctionalInterface
public interface AbstractCronAction {
    void execute() throws Exception;
}
