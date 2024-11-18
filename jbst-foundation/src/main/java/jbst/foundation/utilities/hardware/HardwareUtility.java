package jbst.foundation.utilities.hardware;

import jbst.foundation.domain.hardware.memories.HeapMemory;
import lombok.experimental.UtilityClass;

import static java.lang.management.ManagementFactory.getMemoryMXBean;

@UtilityClass
public class HardwareUtility {

    public static HeapMemory getHeapMemory() {
        var heapMemoryUsage = getMemoryMXBean().getHeapMemoryUsage();
        return new HeapMemory(
                heapMemoryUsage.getInit(),
                heapMemoryUsage.getUsed(),
                heapMemoryUsage.getMax(),
                heapMemoryUsage.getCommitted()
        );
    }
}
