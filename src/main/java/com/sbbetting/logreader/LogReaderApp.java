package com.sbbetting.logreader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LogReaderApp {

    private final File logsDirectory = new File(Config.LOGS_DIRECTORY_PATH);

    public void runApp() {
        if (directoryExists(logsDirectory)) {

            File[] files = logsDirectory.listFiles();
            if (filesExist(files)) {
                System.out.printf("\nReading lines from files in directory: %s\n\n", logsDirectory);

                Arrays.stream(files).sorted((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
                        .forEach(this::processLogFile);

            } else {
                System.out.printf("No files found in directory %s", logsDirectory);
            }
        } else {
            System.out.printf("Directory %s does not exist.", logsDirectory);
        }
    }

    public void processLogFile(File file) {
        long parsingFileStartTimestamp = System.currentTimeMillis();
        Map<String, Integer> severityCounts = new HashMap<>();
        Map<String, Integer> libraryCounts = new HashMap<>();

        try (Stream<String> linesStream = Files.lines(file.toPath())) {
            List<String> lines = linesStream.toList();

            if (!lines.isEmpty()) {
                LocalDateTime firstLogTimestamp = LogParser.parseDateTime(lines.get(0));
                LocalDateTime lastLogTimestamp = LogParser.getLastDateTime(lines);

                if (firstLogTimestamp != null && lastLogTimestamp != null) {
                    lines.forEach(line -> {
                        String severity = LogParser.parseSeverity(line);
                        String library = LogParser.parseLibrary(line);
                        incrementSeverityCounts(severity, severityCounts);
                        incrementLibraryCounts(library, libraryCounts);
                    });

                    long parsingFileEndTimestamp = System.currentTimeMillis();
                    long timeSpentParsingFile = parsingFileEndTimestamp - parsingFileStartTimestamp;
                    long timeBetweenFirstAndLastLog = Math.abs(ChronoUnit.SECONDS.between(firstLogTimestamp, lastLogTimestamp));

                    LogSummaryPrinter.printLogFileSummary(timeSpentParsingFile, timeBetweenFirstAndLastLog, severityCounts, libraryCounts);
                } else {
                    System.out.println("Failed to parse date-time from log entries in the file.");
                }
            } else {
                System.out.println("Log file does not contain any logs");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean directoryExists(File directory) {
        return directory.exists() && directory.isDirectory();
    }

    public boolean filesExist(File[] files) {
        return files != null && files.length > 0;
    }

    public void incrementSeverityCounts(String severity, Map<String, Integer> severityCounts) {
        if (severity != null) {
            severityCounts.put(severity, severityCounts.getOrDefault(severity, 0) + 1);
        }
    }

    public void incrementLibraryCounts(String library, Map<String, Integer> libraryCounts) {
        if (library != null) {
            libraryCounts.put(library, libraryCounts.getOrDefault(library, 0) + 1);
        }
    }
}