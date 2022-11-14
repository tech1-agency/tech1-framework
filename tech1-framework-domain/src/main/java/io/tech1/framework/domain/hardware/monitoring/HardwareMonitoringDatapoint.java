package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.hardware.bytes.ByteSize;
import io.tech1.framework.domain.hardware.bytes.ByteUnit;
import io.tech1.framework.domain.hardware.memories.CpuMemory;
import io.tech1.framework.domain.hardware.memories.GlobalMemory;
import io.tech1.framework.domain.hardware.memories.HeapMemory;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.domain.tuples.TuplePercentage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.hardware.monitoring.HardwareName.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringDatapoint {
    private final ByteUnit unit;

    private final Tuple3<TuplePercentage, TuplePercentage, TuplePercentage> global;
    private final BigDecimal cpu;
    private final TuplePercentage heap;

    private final HardwareMonitoringMaxValues maxValues;

    private final long timestamp;

    public HardwareMonitoringDatapoint(
            GlobalMemory global,
            CpuMemory cpu,
            HeapMemory heap
    ) {
        assertNonNullOrThrow(global, invalidAttribute("MonitoringDatapoint.global"));
        assertNonNullOrThrow(cpu, invalidAttribute("MonitoringDatapoint.cpu"));
        assertNonNullOrThrow(heap, invalidAttribute("MonitoringDatapoint.heap"));
        this.unit = ByteUnit.GIGABYTE;

        var server = TuplePercentage.of(
                new ByteSize(global.getTotal().getBytes() - global.getAvailable().getBytes()).getBy(this.unit),
                global.getTotal().getBy(this.unit),
                2,
                1
        );
        var swap = TuplePercentage.of(
                global.getSwapUsed().getBy(this.unit),
                global.getSwapTotal().getBy(this.unit),
                2,
                1
        );
        var virtual = TuplePercentage.of(
                global.getVirtualUsed().getBy(this.unit),
                global.getVirtualTotal().getBy(this.unit),
                2,
                1
        );

        this.global = Tuple3.of(
                server,
                swap,
                virtual
        );

        this.cpu = cpu.getValue();

        this.heap = TuplePercentage.of(
                heap.getUsed().getBy(this.unit),
                heap.getMax().getBy(this.unit),
                2,
                1
        );

        this.maxValues = new HardwareMonitoringMaxValues(global, heap);

        this.timestamp = getCurrentTimestamp();
    }

    public static HardwareMonitoringDatapoint zeroUsage() {
        return new HardwareMonitoringDatapoint(
                GlobalMemory.zeroUsage(),
                CpuMemory.zeroUsage(),
                HeapMemory.zeroUsage()
        );
    }

    public HardwareMonitoringDatapointTableView tableView(
            HardwareMonitoringThresholds thresholds
    ) {
        List<HardwareMonitoringDatapointTableRow> table = new ArrayList<>();

        table.add(
                new HardwareMonitoringDatapointTableRow(
                        CPU,
                        this.timestamp,
                        this.cpu,
                        "",
                        thresholds
                )
        );

        Function<Tuple3<HardwareName, TuplePercentage, ByteSize>, HardwareMonitoringDatapointTableRow> tableRowFnc = tuple3 -> {
            var hardwareName = tuple3.getA();
            var percentage = tuple3.getB().getPercentage();
            var readableValue = tuple3.getB().getValue() + " " + this.unit.getSymbol() + " of " + tuple3.getC().getBy(this.unit, 2) + " " + this.unit.getSymbol();
            return new HardwareMonitoringDatapointTableRow(
                    hardwareName,
                    this.timestamp,
                    percentage,
                    readableValue,
                    thresholds
            );
        };

        table.add(tableRowFnc.apply(Tuple3.of(HEAP, this.heap, this.maxValues.getHeap())));
        table.add(tableRowFnc.apply(Tuple3.of(SERVER, this.global.getA(), this.maxValues.getServer())));
        table.add(tableRowFnc.apply(Tuple3.of(SWAP, this.global.getB(), this.maxValues.getSwap())));
        table.add(tableRowFnc.apply(Tuple3.of(VIRTUAL, this.global.getC(), this.maxValues.getVirtual())));

        return new HardwareMonitoringDatapointTableView(table);
    }
}
