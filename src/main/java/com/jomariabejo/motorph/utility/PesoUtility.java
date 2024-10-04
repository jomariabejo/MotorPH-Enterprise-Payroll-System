package com.jomariabejo.motorph.utility;

import java.text.DecimalFormat;

public class PesoUtility {

    private static final String PESO_SIGN = "₱";

    /**
     * Format a numeric string to Peso currency format with four decimal places.
     *
     * @param amount The numeric string to format.
     * @return The formatted Peso currency string.
     * @throws IllegalArgumentException If the input is not a valid numeric string.
     */
    public static String formatToPeso(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty.");
        }

        // Remove any non-numeric characters except for decimal point
        String cleanAmount = amount.replaceAll("[^0-9.]", "");

        // Try to parse the cleaned string to a number
        try {
            double numericAmount = Double.parseDouble(cleanAmount);

            // Format the number with four decimal places and thousands separators
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000");
            String formattedAmount = decimalFormat.format(numericAmount);

            // Add the Peso sign
            return PESO_SIGN + formattedAmount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount provided. Please provide a numeric string.", e);
        }
    }
}
