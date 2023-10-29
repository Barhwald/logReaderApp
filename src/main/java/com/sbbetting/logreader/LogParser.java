package com.sbbetting.logreader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LogParser {

    private LogParser() {
    }

    public static String parseSeverity(String line) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3} (\\w+)");
        Matcher matcher = pattern.matcher(line);

        String severityLevel = null;
        if (matcher.find()) {
            severityLevel = matcher.group(1);
        }
        return severityLevel;
    }

    public static String parseLibrary(String line) {
        Pattern pattern = Pattern.compile("\\[([^]]+)]");
        Matcher matcher = pattern.matcher(line);

        String library = null;
        if (matcher.find()) {
            library = matcher.group(1);
        }
        return library;
    }

    public static LocalDateTime parseDateTime(String logEntry) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})");
        Matcher matcher = pattern.matcher(logEntry);

        if (matcher.find()) {
            String dateTimeString = matcher.group(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
            return LocalDateTime.parse(dateTimeString, formatter);
        }
        return null;
    }

    public static LocalDateTime getLastDateTime(List<String> lines) {
        List<LocalDateTime> timeStamps = getAllDateTimes(lines);
        return timeStamps.get(timeStamps.size()-1);
    }

    public static List<LocalDateTime> getAllDateTimes(List<String> lines) {
        List<LocalDateTime> allDateTimes = new ArrayList<>();

        for (String line : lines) {
            LocalDateTime timeStamp = parseDateTime(line);
            if (timeStamp != null) {
                allDateTimes.add(timeStamp);
            }
        }
        return allDateTimes;
    }
}
