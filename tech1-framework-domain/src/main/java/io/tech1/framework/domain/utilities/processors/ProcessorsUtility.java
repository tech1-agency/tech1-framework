package io.tech1.framework.domain.utilities.processors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcessorsUtility {
    public static int getNumOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getHalfOfCores() {
        return Runtime.getRuntime().availableProcessors() * 5 / 10;
    }
}
