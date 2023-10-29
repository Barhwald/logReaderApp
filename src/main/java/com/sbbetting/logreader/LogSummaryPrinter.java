package com.sbbetting.logreader;

import java.util.Map;
public final class LogSummaryPrinter {

    private LogSummaryPrinter() {
    }

    public static void printLogFileSummary(long timeSpentParsingFile, long timeBetweenFirstAndLastLog,
                                           Map<String, Integer> severityCounts, Map<String, Integer>libraryCounts) {
        System.out.printf("Time spent per file: %d milliseconds\n", timeSpentParsingFile);
        TimePrinter.printDurationInDHMSFormat(TimeConverter.getDuration(timeBetweenFirstAndLastLog));
        printLogsBySeverity(severityCounts);
        printErrorToOthersRatio(severityCounts);
        printLogsByLibrary(libraryCounts);
        System.out.println("\n" + "-".repeat(30));

    }

    public static void printLogsBySeverity(Map<String, Integer> severityCounts) {
        System.out.println("\nLogs count based on their severity: ");
        for (var severityCount : severityCounts.entrySet()) {
            System.out.printf("- %s : %d\n", severityCount.getKey(), severityCount.getValue());
        }
    }

    public static void printLogsByLibrary(Map<String, Integer> libraryCounts) {
        System.out.println("\nLogs count based on their library: ");
        for (var libraryCount : libraryCounts.entrySet()) {
            System.out.printf("- %s : %d\n", libraryCount.getKey(), libraryCount.getValue());
        }
    }

    public static void printErrorToOthersRatio(Map<String, Integer> severityCounts) {

        long errors = severityCounts.getOrDefault("ERROR", 0);
        long fatals = severityCounts.getOrDefault("FATAL", 0);
        long highSeveritiesCount = errors + fatals;
        long others = severityCounts.entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals("ERROR") && !entry.getKey().equals("FATAL"))
                .mapToInt(Map.Entry::getValue)
                .sum();

        double quotient = (double) highSeveritiesCount / (highSeveritiesCount+others);
        double errorPercentage = quotient * 100;
        String formattedPercentage = String.format("%.2f%%", errorPercentage);
        System.out.printf("\nERROR and higher severities count to other severities ratio is %d : %d. This is %s\n",
                highSeveritiesCount, others, formattedPercentage);
    }

}
