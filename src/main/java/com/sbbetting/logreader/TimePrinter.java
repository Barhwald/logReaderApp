package com.sbbetting.logreader;

import java.time.Duration;

public final class TimePrinter {

    private TimePrinter() {
    }

    public static void printDurationInDHMSFormat(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        System.out.printf("\nTime between first and last log: %d days, %d hours, %d minutes, %d seconds\n", days, hours, minutes, seconds);
    }
}
