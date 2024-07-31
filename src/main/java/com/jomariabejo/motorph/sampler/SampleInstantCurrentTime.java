package com.jomariabejo.motorph.sampler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SampleInstantCurrentTime {
    public static void main(String[] args) {
        // Specify the Philippine timezone
        ZoneId philippinesZoneId = ZoneId.of("Asia/Manila");

        // Get the current LocalDateTime in the Philippine timezone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(philippinesZoneId);

        // Convert ZonedDateTime to Instant
        Instant instant = zonedDateTime.toInstant();

        // Print the Instant in UTC
        System.out.println("Instant in UTC: " + instant);

        // Optionally, format ZonedDateTime for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .withZone(philippinesZoneId);
        String formattedDateTime = formatter.format(zonedDateTime);

        // Print the formatted LocalDateTime in the Philippine timezone
        System.out.println("LocalDateTime in Asia/Manila timezone: " + formattedDateTime);
    }
}
