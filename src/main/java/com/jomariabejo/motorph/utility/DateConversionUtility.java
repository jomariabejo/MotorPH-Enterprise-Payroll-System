package com.jomariabejo.motorph.utility;

import java.sql.Date;

public class DateConversionUtility {

    public static Date toSqlDate(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new Date(utilDate.getTime());
    }
}