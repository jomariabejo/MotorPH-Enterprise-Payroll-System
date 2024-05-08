package com.jomariabejo.motorph.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtility {

    public static BigDecimal createBigDecimal(String numberString) {
        try {
            // Parse the string to BigDecimal
            BigDecimal number = new BigDecimal(numberString);

            // Check if the number has any decimal places
            if (number.scale() == 0) {
                // If the number has no decimal places, append ".0000"
                number = number.setScale(0, RoundingMode.UNNECESSARY);
                number = number.setScale(4);
            } else {
                // Scale the BigDecimal to a maximum of four decimal places
                number = number.setScale(4, RoundingMode.HALF_UP);
            }

            return number;
        } catch (NumberFormatException e) {
            // Handle the case when the string cannot be parsed to BigDecimal
            e.printStackTrace();
            return null;
        }
    }
}
