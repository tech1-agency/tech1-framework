package jbst.foundation.utilities.printer;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

import static java.lang.Math.max;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static jbst.foundation.domain.constants.JbstConstants.Symbols.DASH;

@SuppressWarnings("unused")
@UtilityClass
public class Printer {

    public static void printTable(String[] tableHeaders, Object[][] tableCells) {
        var columnWidths = Arrays.stream(tableHeaders).mapToInt(String::length).toArray();

        Arrays.stream(tableCells).forEach(row -> range(0, row.length).forEach(i -> columnWidths[i] = max(columnWidths[i], row[i].toString().length())));

        var rowFormat = Arrays.stream(columnWidths).mapToObj(width -> "%-" + (width + 2) + "s").collect(joining());

        Arrays.stream(columnWidths).mapToObj(width -> DASH.repeat(width + 2)).forEach(System.out::print);
        System.out.println();

        System.out.printf(rowFormat + "%n", (Object[]) tableHeaders);

        Arrays.stream(columnWidths).mapToObj(width -> DASH.repeat(width + 2)).forEach(System.out::print);
        System.out.println();

        Arrays.stream(tableCells).forEach(row -> System.out.printf(rowFormat + "%n", row));
        System.out.println();
    }
}
