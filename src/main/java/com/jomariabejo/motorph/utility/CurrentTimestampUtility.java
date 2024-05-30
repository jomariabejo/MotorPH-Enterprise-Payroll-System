package com.jomariabejo.motorph.utility;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentTimestampUtility {
    public static Timestamp getCurrentTimestamp() {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Create a Timestamp object with the current time
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);

        return currentTimestamp;
    }

    public static Time getCurrentTime() {
        // Get the current time
        LocalTime now = LocalTime.now();
        LocalTime formattedTime = now.withSecond(0).withNano(0);
        return Time.valueOf(formattedTime);
    }


    public static String getCurrentTimeFormatted() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}


