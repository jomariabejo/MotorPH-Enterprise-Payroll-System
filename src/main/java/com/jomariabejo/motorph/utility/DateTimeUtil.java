package com.jomariabejo.motorph.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    // Define the timezone for the Philippines
    private static final ZoneId PHILIPPINE_TIMEZONE = ZoneId.of("Asia/Manila");

    /**
     * Returns the current date and time in the Philippines timezone.
     *
     * @return ZonedDateTime representing the current date and time in the Philippines timezone
     */
    public static ZonedDateTime getCurrentDateTimeInPhilippines() {
        return ZonedDateTime.now(PHILIPPINE_TIMEZONE);
    }
    
    /**
     * Returns the current instant in UTC.
     *
     * @return Instant representing the current time in UTC
     */
    public static Instant getCurrentInstant() {
        return Instant.now();
    }
}
