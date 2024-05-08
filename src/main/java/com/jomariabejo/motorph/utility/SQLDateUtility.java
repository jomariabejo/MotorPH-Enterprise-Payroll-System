package com.jomariabejo.motorph.utility;

import java.sql.Date;

public class SQLDateUtility {

    public static int getDifferenceInDays(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long difference = endTime - startTime;

        // Convert milliseconds to days
        int daysDifference = (int) (difference / (1000 * 60 * 60 * 24));

        // If dates are equal, consider it as one day
        if (daysDifference == 0 && !startDate.after(endDate)) {
            return 1;
        }

        return Math.abs(daysDifference);
    }
}
