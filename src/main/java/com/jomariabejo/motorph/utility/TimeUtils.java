package com.jomariabejo.motorph.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Utility class for calculating time differences.
 */
public final class TimeUtils {

    // Private constructor to prevent instantiation
    private TimeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Calculates the difference between two LocalTime instances.
     *
     * @param startTime The start time.
     * @param endTime The end time.
     * @return The difference between the two times in seconds, as a float with precision 10 and scale 2.
     */
    public static float calculateTimeDifference(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        // Calculate the duration between the two times
        Duration duration = Duration.between(startTime, endTime);
        // Get the difference in seconds as a double
        double seconds = duration.getSeconds();
        // Convert to BigDecimal with precision 10 and scale 2
        BigDecimal bigDecimalSeconds = new BigDecimal(seconds).setScale(2, RoundingMode.HALF_UP);
        // Return as float
        return bigDecimalSeconds.floatValue();
    }
}
