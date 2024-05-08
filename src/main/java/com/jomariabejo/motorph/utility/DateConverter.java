package com.jomariabejo.motorph.utility;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConverter {
    public static Date convertToMySQLDate(String dateString) {
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            return (Date) inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date today() {
        // Create a SimpleDateFormat object with MySQL date format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Get the current date and format it to MySQL date format
            java.util.Date currentDate = new java.util.Date();
            String formattedDate = dateFormat.format(currentDate);

            // Convert the formatted date string back to java.sql.Date
            return Date.valueOf(formattedDate);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseSqlDate(String inputDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = dateFormat.parse(inputDate);
        return new Date(parsedDate.getTime());
    }

}
