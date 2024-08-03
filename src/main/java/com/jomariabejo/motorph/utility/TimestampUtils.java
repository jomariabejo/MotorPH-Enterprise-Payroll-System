package com.jomariabejo.motorph.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimestampUtils {

    private static final DateTimeFormatter MYSQL_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Gets the current timestamp in MySQL format.
     *
     * @return the current timestamp as a string in 'yyyy-MM-dd HH:mm:ss' format.
     */
    public static String getCurrentTimestamp() {
        Instant now = Instant.now();
        return formatInstant(now);
    }

    /**
     * Converts a given Instant to MySQL format.
     *
     * @param instant the Instant to be formatted.
     * @return the formatted timestamp as a string in 'yyyy-MM-dd HH:mm:ss' format.
     */
    public static String formatInstant(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return MYSQL_TIMESTAMP_FORMAT.format(zonedDateTime);
    }

    /**
     * Parses a MySQL timestamp string to an Instant.
     *
     * @param mysqlTimestamp the MySQL timestamp string.
     * @return the Instant representation of the timestamp.
     * @throws DateTimeParseException if the text cannot be parsed.
     */
    public static Instant parseMysqlTimestamp(String mysqlTimestamp) throws DateTimeParseException {
        LocalDateTime localDateTime = LocalDateTime.parse(mysqlTimestamp, MYSQL_TIMESTAMP_FORMAT);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static void main(String[] args) {
        // Print the current timestamp in MySQL format
        System.out.println("Current Timestamp: " + getCurrentTimestamp());

        // Print a timestamp from an Instant
        Instant exampleInstant = Instant.now();
        System.out.println("Timestamp from Instant: " + formatInstant(exampleInstant));

        // Example of parsing a MySQL timestamp string to an Instant
        try {
            String mysqlTimestamp = "2024-08-03 07:53:14";
            Instant instant = parseMysqlTimestamp(mysqlTimestamp);
            System.out.println("Parsed Instant: " + instant);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing timestamp: " + e.getMessage());
        }
    }
}
