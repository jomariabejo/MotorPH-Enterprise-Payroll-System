package com.jomariabejo.motorph.utility;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;

public class HoursWorkedCalculator {
    public static BigDecimal computeOverTimeHoursWorked(Time time) {
        // Convert the Time object to LocalTime
        LocalTime timeOut = time.toLocalTime();

        // Define the cutoff time as 17:00:00
        LocalTime cutoffTime = LocalTime.of(17, 0, 0);

        // Calculate overtime hours worked
        if (timeOut.isAfter(cutoffTime)) {
            // Subtract the cutoff time from the actual time out
            long seconds = cutoffTime.until(timeOut, java.time.temporal.ChronoUnit.SECONDS);
            BigDecimal overtimeHours = BigDecimal.valueOf(seconds).divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_HALF_UP);
            return overtimeHours;
        } else {
            // Return 0 if time out is before or equal to 17:00:00
            return BigDecimal.ZERO;
        }
    }
}
