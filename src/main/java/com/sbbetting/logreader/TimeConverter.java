package com.sbbetting.logreader;

import java.time.Duration;

public final class TimeConverter {

    private TimeConverter() {
    }

    public static Duration getDuration(long totalSeconds) {
        return Duration.ofSeconds(totalSeconds);
    }

}
