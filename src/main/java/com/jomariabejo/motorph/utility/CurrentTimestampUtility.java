package com.jomariabejo.motorph.utility;

import java.sql.Timestamp;

public class CurrentTimestampUtility {
    public static Timestamp getCurrentTimestamp() {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Create a Timestamp object with the current time
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);

        return currentTimestamp;
    }
}
